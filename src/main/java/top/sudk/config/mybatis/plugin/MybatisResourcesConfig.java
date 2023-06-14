package top.sudk.config.mybatis.plugin;


import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author sudingkun
 */
@Slf4j
@DependsOn(value = {"sqlSessionFactory"})
@Configuration
@RequiredArgsConstructor
public class MybatisResourcesConfig implements ApplicationRunner {

    /**
     * namespace + id + type : .xml text
     */
    private final Map<String, String> sqlResourceMap = new HashMap<>();

    /**
     * namespace + id + type : .xml path
     */
    private final Map<String, String> sqlResourcePathMap = new HashMap<>();


    private final MybatisPlusProperties mybatisPlusProperties;

    @Override
    public void run(ApplicationArguments args) {
        initMybatisResources();
        log.info("sqlResourceMap size: {}", sqlResourceMap.size());
        log.info("sqlResourcePathMap size: {}", sqlResourcePathMap.size());
    }


    private void initMybatisResources() {
        SqlSessionFactory factory = SqlHelper.FACTORY;
        org.apache.ibatis.session.Configuration configuration = factory.getConfiguration();

        Resource[] mapperLocations = mybatisPlusProperties.resolveMapperLocations();
        for (Resource mapperLocation : mapperLocations) {
            try {
                if (mapperLocation == null) {
                    continue;
                }

                String absolutePath = mapperLocation.getURL().getPath();

                // filePath + source -> namespace + id + type
                XPathParser xPathParser = new XPathParser(mapperLocation.getInputStream(), true, configuration.getVariables(), new XMLMapperEntityResolver());
                List<XNode> xNodes = xPathParser.evalNodes("/mapper");
                for (XNode context : xNodes) {
                    String namespace = context.getStringAttribute("namespace");
                    List<XNode> list = context.evalNodes("select|insert|update|delete");
                    for (XNode xNode : list) {
                        String id = xNode.getStringAttribute("id");
                        String nodeName = xNode.getNode().getNodeName();
                        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
                        String key = namespace + "." + id + "#" + sqlCommandType;
                        sqlResourceMap.put(key, new String(FileCopyUtils.copyToByteArray(mapperLocation.getInputStream())));
                        sqlResourcePathMap.put(key, absolutePath);
                    }
                }


            } catch (Exception e) {
                // ignore
                log.error("initMybatisResources error", e);
            }
        }
    }


    public String getSqlResource(String namespace, String id, SqlCommandType sqlCommandType) {
        return sqlResourceMap.get(namespace + "." + id + "#" + sqlCommandType);
    }

    public String getSqlResourcePatch(String namespace, String id, SqlCommandType sqlCommandType) {
        return sqlResourcePathMap.get(namespace + "." + id + "#" + sqlCommandType);
    }


}
