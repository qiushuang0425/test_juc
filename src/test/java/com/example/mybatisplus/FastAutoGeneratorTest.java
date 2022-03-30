package com.example.mybatisplus;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;

import java.util.Collections;

/**
 * @author qius
 */
public class FastAutoGeneratorTest {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://192.168.162.138:3306/fin_xw_dev?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&rewriteBatchedStatements=true&useSSL=false",
            "root",
            "GCF1qaz2wsx!@#")
            .typeConvert(new MySqlTypeConvertCustom());


    /**
     * 执行 run
     */
    public static void main(String[] args) {
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> builder
                        // 设置作者
                        .author("Glodon")
                        // 开启 swagger 模式
                        .enableSwagger()
                        // 覆盖已生成文件
                        .fileOverride()
                        // 指定输出目录
                        .outputDir("D://mpg"))
                .packageConfig(builder -> builder
                        // 设置父包名
                        .parent("com.glodon.fin.xwbank.dao")
                        .entity("model.generate")
                        .mapper("dao.generate")
                        // 设置mapperXml生成路径
                        .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D://mpg")))
                .strategyConfig(builder -> {
                    builder
                            // 设置需要生成的表名
                            .addInclude("fin_xw_requisition", "fin_xw_quota")
                            // 设置过滤表前缀
                            .addTablePrefix("fin_");

                    builder.entityBuilder()
                            .disableSerialVersionUID()
                            .enableLombok()
                            .logicDeleteColumnName("deleted")
                            .logicDeletePropertyName("deleted")
                            .addTableFills(new Column("create_time", FieldFill.INSERT), new Column("deleted", FieldFill.INSERT))
                            .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE));

                    builder.serviceBuilder()
                            .formatServiceFileName("Base%sService")
                            .formatServiceImplFileName("Base%sServiceImp");

                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .templateConfig(builder -> builder.controller(""))
                .execute();
    }

}


/**
 * 自定义类型转换
 */
class MySqlTypeConvertCustom extends MySqlTypeConvert implements ITypeConvert {
    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("tinyint(1)")) {
            return DbColumnType.INTEGER;
        }
        if (t.contains("timestamp")) {
            return DbColumnType.DATE;
        }
        return super.processTypeConvert(globalConfig, fieldType);
    }
}