<template>
    <div class="login-warp">
        <div class="login-main">
            <div class="title">登录</div>
            <div class="main">
                <el-form :model="form" status-icon :rules="rules" ref="loginFrom"
                         label-width="100px"
                         class="demo-ruleForm">
                    <el-form-item prop="uname">
                        <el-input type="text" v-model="form.uname" autocomplete="off" placeholder="请输入用户名"
                                  clearable></el-input>
                    </el-form-item>
                    <el-form-item prop="upwd">
                        <el-input type="password" v-model="form.upwd" autocomplete="off" placeholder="请输入密码" clearable
                                  show-password></el-input>
                    </el-form-item>
                    <el-form-item>
                        <div class="login-btn">
                            <el-button type="primary" @click="submitForm('loginFrom')">登录</el-button>
                        </div>
                    </el-form-item>
                    <div style="text-align: center;color: #898989;margin-bottom: 10px">
                       还没注册&nbsp;？<a href="register" style="color: #409eff">去注册</a>
                    </div>

                </el-form>
            </div>
        </div>
    </div>
</template>
<script>
    import {ElMessage} from "element-plus";

    export default {
        data() {
            return {
                form: {
                    uname: '',
                    upwd: '',
                },
                rules: {
                    uname: [
                        {required: true, message: "请输入用户名", trigger: "blur"}
                    ],
                    upwd: [
                        {required: true, message: "请输入密码", trigger: 'blur'}
                    ]
                }
            };
        },
        created() {
        },
        methods: {
            submitForm(formName) {
                let that = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        const formData = new FormData();
                        formData.append("uname", this.form.uname)
                        formData.append("upwd", this.form.upwd)
                        this.$http.post("http://localhost:8081/api/user/login", formData)
                            .then(res => {
                                if (res.code == 20000) {
                                    let dd={
                                        token:res.data.token,
                                        uid:res.data.uid
                                    }
                                    that.$store.dispatch("set_token",dd)
                                    ElMessage.success('登录成功')
                                    that.$router.push('/index');
                                }
                                console.log(res)
                                console.log(that.$store.state.token)
                            })

                    } else {
                        ElMessage.error('密码账号错误')
                        console.log('error submit!!');
                        return false;
                    }
                });
            }
        }
    }
</script>

<style>
    .login-warp {
        display: flex;
        height: 100vh;
        width: 100vw;
        justify-content: center;
        align-items: center;
        background: url("../../assets/background.jpg");
    }

    .login-main {
        display: flex;
        flex-direction: column;
        justify-content: center;
        width: 350px;

        background: #fff;
    }

    .title {
        line-height: 50px;
        text-align: center;
        font-size: 20px;
        border-bottom: 1px solid #ddd;
    }

    .main {
        margin-top: 30px;
        display: flex;
    }

    .main, .el-form {
        width: 100%;
        height: 100%;
    }

    .el-form, .el-form-item__content {
        margin: 0 auto !important;
        width: 240px;
    }

    .login-btn .el-button {
        width: 240px;
    }
</style>
