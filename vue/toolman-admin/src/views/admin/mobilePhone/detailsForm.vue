<!--手机详情页-->
<template>
    <div class="mpDetails">
        <el-descriptions
                class="margin-top"
                title="品牌："
                :column="3"
                border
        >
            <template #extra>
                <el-button type="primary" size="small">操作</el-button>
            </template>
            <el-descriptions-item>
                <template #label>
                    商品名称：
                </template>
                小米Redmi 9A
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    商品编号：
                </template>
                18100000000
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    商品毛重
                </template>
                367.00g
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    商品场地：
                </template>
                中国大陆
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    CPU型号：
                </template>
                其他
            </el-descriptions-item>
        </el-descriptions>

    </div>

</template>

<script>
    export default {
        data() {
            return {
                mpDetails: {}//手机详情

            }
        },
    }
</script>
<style scoped>
    .mpDetails {
        margin: 25px 0;
    }
</style>