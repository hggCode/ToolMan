<!--手机列表-->
<template>
    <div class="app-container">
        <!--查询表单-->
        <el-form :inline="true">
            <el-form-item>
                <el-form-item>
                    <el-autocomplete
                            v-model="searchObj.name"
                            :trigger-on-focus="false"
                            class="inline-input"
                            placeholder="手机名称"
                            value-key="name"/>
                </el-form-item>
            </el-form-item>

            <el-form-item>
                <el-autocomplete v-model="searchObj.phone" placeholder="品牌"/>
            </el-form-item>
            <el-form-item>
                <el-autocomplete v-model="searchObj.phone" placeholder="CPU处理器"/>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="fetchData()">查询</el-button>
                <el-button type="default" @click="resetData()">清空</el-button>
            </el-form-item>
        </el-form>

        <!-- 添加产品 -->
        <div class="addMp">
            <router-link to="updateMpForm">
                <el-button title="添加商品" icon="el-icon-circle-plus-outline"></el-button>
            </router-link>
        </div>

        <!-- 产品列表 -->
        <el-table :data="tableData" border style="text-align: center">
            <el-table-column prop="date" label="序号" width="100"></el-table-column>
            <el-table-column label="图像">
                <img :src="require('D:\\JavaStudy\\ToolMan\\vue\\toolman-admin\\public\\1.jpg')"
                     style="height: 120px">
            </el-table-column>
            <el-table-column prop="province" label="机型"></el-table-column>
            <el-table-column prop="city" label="价格"></el-table-column>
            <el-table-column prop="address" label="CPU"></el-table-column>
            <el-table-column prop="address" label="品牌"></el-table-column>
            <el-table-column prop="address" label="收藏数"></el-table-column>
            <el-table-column label="操作" width="120">
                <template #default="scope">
                    <router-link to="detailsFrom">
                        <el-button @click="handleClick(scope.row)" type="text" size="small">详情</el-button>
                    </router-link>
                    <router-link to="updateMpForm">
                        <el-button type="text" size="small">编辑</el-button>
                    </router-link>
                    <el-button type="text" size="small">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="block">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    page-count="5"
                    :page-sizes="[5, 10, 15, 20]"
                    :page-size="5"
                    layout="total, sizes, prev, pager, next,jumper"
                    :total="100"
            >
            </el-pagination>
        </div>
    </div>
</template>

<script>
    export default {
        methods: {
            handleClick(row) {
                console.log(row)
            },
            //查询手机数据
            fetchData() {

            },
            //清空输入框数据
            resetData() {
            },
            handleSizeChange(val) {
                console.log(`每页 ${val} 条`)
            },
            handleCurrentChange(val) {
                console.log(`当前页: ${val}`)
            },
        },

        data() {
            return {
                total: 0, // 总记录数
                page: 1, // 页码
                limit: 5, // 每页记录数
                searchObj: {},//查询条件
                currentPage: 5,//跳转页码
                tableData: [
                    {
                        date: '2016-05-02',
                        path: "public/favicon.ico",
                        province: '上海',
                        city: '普陀区',
                        address: '上海市普陀区金沙江路 1518 弄',
                        zip: 200333,
                    },
                    {
                        date: '2016-05-04',
                        path: 'public/favicon.ico',
                        province: '上海',
                        city: '普陀区',
                        address: '上海市普陀区金沙江路 1517 弄',
                        zip: 200333,
                    },
                    {
                        date: '2016-05-01',
                        path: 'public/favicon.ico',
                        province: '上海',
                        city: '普陀区',
                        address: '上海市普陀区金沙江路 1519 弄',
                        zip: 200333,
                    },
                    {
                        date: '2016-05-03',
                        path: 'public/favicon.ico',
                        province: '上海',
                        city: '普陀区',
                        address: '上海市普陀区金沙江路 1516 弄',
                        zip: 200333,
                    },
                ],
            }
        },
    }
</script>
<style scoped>
    /deep/ .cell {
        text-align: center;
        /*background-color: black;*/
    }

    .el-button--text {
        margin: 0 2px;
    }

    .block {
        margin: 25px 0 50px 0;
    }

    .el-form {
        margin-top: 20px;
        display: inline-block;
    }

    .addMp {
        margin-left: 357px;
        display: inline-block;
    }
</style>