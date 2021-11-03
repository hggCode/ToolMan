package work.wlwl.toolman.service.reptile;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import org.junit.jupiter.api.Test;
import work.wlwl.toolman.service.base.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class genCode {

    @Test
    void gen(){
//        设置模块名
        String moduleName="reptile";
//        要生成的表
        List<String> list=new ArrayList<>();
//        list.add("reptile_brand");
//        list.add("reptile_color");
//        list.add("reptile_edition");
//        list.add("reptile_product");
//        list.add("reptile_product_img");
        list.add("reptile_property");
//        忽略表的前缀
        String prefix="reptile_";


        String projectPath = System.getProperty("user.dir");
        FastAutoGenerator.create("jdbc:mysql://119.29.163.168:3306/toolman?serverTimezone=GMT%2B8", "root", "Hgg0720.")
                .globalConfig(builder -> {
                    builder.author("hgg&fbb") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir()  //生成后不打开目录
                            .dateType(DateType.TIME_PACK) //定义生成的实体类中日期类型
                            .outputDir(projectPath+"/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("work.wlwl.toolman.service") // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, projectPath+"/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(list) // 设置需要生成的表名
                            .addTablePrefix(prefix) // 设置过滤表前缀
                            .entityBuilder()  //配置实体类
                            .superClass(BaseEntity.class)  //配置父类
                            .enableChainModel()  //开启链式
                            .enableLombok() //Lombok模型
                            .enableRemoveIsPrefix()//去掉逻辑字段中的is_前缀
                            .naming(NamingStrategy.underline_to_camel) //数据库表映射到实体的命名策略
                            .columnNaming(NamingStrategy.underline_to_camel) //数据库表字段映射到实体的命名策略
                            .logicDeleteColumnName("is_deleted")  //逻辑删除字段名(数据库)
                            .logicDeletePropertyName("isDeleted") //逻辑删除属性名(实体)
                            .addSuperEntityColumns("id", "gmt_create", "gmt_modified") //设置父类的公共字段
                            .addTableFills(new Column("gmt_create", FieldFill.INSERT))
                            .addTableFills(new Column("gmt_modified", FieldFill.INSERT_UPDATE))
                            .addTableFills(new Property("gmtCreate", FieldFill.INSERT))
                            .addTableFills(new Property("gmtModified", FieldFill.INSERT_UPDATE))
                            .serviceBuilder() //配置service
                            .formatServiceFileName("%sService") // //去除IService 的 I
                            .controllerBuilder() //controller 配置
                            .enableRestStyle()  //restful api风格控制器
                            .enableHyphenStyle() //url中驼峰转连字符
                            .build();  //执行


                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
