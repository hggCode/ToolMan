<template>
    <div class="updateMp">
        <el-form ref="form" :model="form" label-width="100px">
            <el-form-item style="width: 30%" label="产品名">
                <el-input v-model="form.name"/>
            </el-form-item>
            <el-form-item style="width: 30%" label="手机尺寸">
                <el-input v-model="form.size"/>
            </el-form-item>
            <el-form-item style="width: 30%" label="重量">
                <el-input v-model="form.weight"/>
            </el-form-item>
            <el-form-item style="width: 30%" label="材质">
                <el-input v-model="form.screen_type"/>
            </el-form-item>
            <el-form-item style="width: 30%" label="主屏尺寸">
                <el-input v-model="form.screen_size"/>
            </el-form-item>
            <el-form-item style="width: 30%" label="屏幕刷新">
                <el-input v-model="form.display_refresh"/>
            </el-form-item>
            <el-form-item style="width: 30%" label="分辨率">
                <el-input v-model="form.qvga"/>
            </el-form-item>
            <el-form-item style="width: 30%" label="处理器">
                <el-input v-model="form.cpu"/>
            </el-form-item>
            <el-form-item style="width: 30%" label="nfc">
                <el-input v-model="form.nfc"/>
            </el-form-item>
            <el-form-item style="width: 30%" label="充电功率">
                <el-input v-model="form.chargerr_power"></el-input>
            </el-form-item>
            <el-form-item label="充电口类型">
                <el-select v-model="form.charger_port" placeholder="请选择">
                    <el-option value="1" label="Micro USB"/>
                    <el-option value="2" label="Type-C"/>
                    <el-option value="3" label="Lightning"/>
                </el-select>
            </el-form-item>
            <el-form-item label="SIM卡类型">
                <el-select v-model="form.sim_type" placeholder="请选择">
                    <el-option value="1" label="Standard"/>
                    <el-option value="2" label="Mini"/>
                    <el-option value="3" label="Micro"/>
                    <el-option value="4" label="Nano"/>
                </el-select>
            </el-form-item>
            <el-form-item label="5G">
                <el-input v-model="form.net_5g"/>
            </el-form-item>
            <el-form-item label="4G">
                <el-input v-model="form.net_4g"/>
            </el-form-item>
            <el-form-item label="3G">
                <el-input v-model="form.net_3g"></el-input>
            </el-form-item>
            <el-form-item label="前置摄像头">
                <el-input v-model="form.befort_camera"></el-input>
            </el-form-item>
            <el-form-item label="后置摄像头">
                <el-input v-model="form.after_camera"></el-input>
            </el-form-item>
            <el-form-item label="插SIM卡数量">
                <el-input-number
                        v-model="form.sim_num"
                        :min="1"
                        :max="2"
                ></el-input-number>
            </el-form-item>
            <el-form-item label="价格">
                <el-input-number
                        v-model="form.num"
                        :min="0"
                ></el-input-number>
            </el-form-item>

            <el-form-item label="手机图片">
                <el-upload
                        class="avatar-uploader"
                        action="https://jsonplaceholder.typicode.com/posts/"
                        :show-file-list="false"
                        :on-success="handleAvatarSuccess"
                        :before-upload="beforeAvatarUpload"
                >
                    <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar"/>
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="onSubmit">保存</el-button>
                <el-button>取消</el-button>
            </el-form-item>
        </el-form>

    </div>
</template>

<script>
    export default {
        data() {
            return {
                form: {
                    name: 'apple',//产品名
                    size: '',//手机尺寸
                    weight: '',//重量
                    screen_type: '',//材质
                    screen_size: '',//主屏尺寸
                    display_refresh: '',//屏幕刷新
                    qvga: '',//分辨率
                    cpu: '',//处理器
                    charger_port: '',//充电口类型
                    av_out: '',//耳机接口类型
                    nfc: '',//nfc
                    sim_type: '',//SIM类型
                    sim_num: '',//SIM数量
                    net_5g: '',
                    net_4g: '',
                    net_3g: '',
                    chargerr_power: '',//充电功率
                    befort_camera: '',//前置摄像头
                    after_camera: '',//后置摄像头
                    num: '',//价格
                    imageUrl: ''
                }
            }
        },
        methods: {
            onSubmit() {
                console.log('submit!')
            },
            handleAvatarSuccess(res, file) {
                this.imageUrl = URL.createObjectURL(file.raw)
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg'
                const isLt2M = file.size / 1024 / 1024 < 2

                if (!isJPG) {
                    this.$message.error('上传头像图片只能是 JPG 格式!')
                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 2MB!')
                }
                return isJPG && isLt2M
            },
        },
    }
</script>
<style>
    element.style {
        width: 80px;
    }

    .updateMp {
        margin: 25px 0;
    }

    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #409eff;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }

    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }

</style>