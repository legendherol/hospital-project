<template>
  <div class="app-container">
      <el-form ref="form" :model="form"  label-width="80px">
        <el-form-item label="医生名字" prop="username">
          <el-input v-model="form.username" placeholder="请输入医生名字" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别" >
            <el-option
              v-for="dict in dict.type.sys_user_sex"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
              :disabled="true"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="电话号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话号码" />
        </el-form-item>
        <el-form-item label="用户id" prop="userId" >
          <el-input v-model="form.userId" placeholder="请输入用户id" :disabled="userIdFlage"/>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="form.age" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="科室" prop="deptId">
          <el-input v-model="form.office.officeName" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="医生简介" prop="position">
          <el-input v-model="form.position" placeholder="请输入医生简介" />
        </el-form-item>
      </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="upRSA">更新密钥对</el-button>
    </div>
  </div>
</template>

<script>

import {delDoctor, findByPhone, upRSA} from "@/api/doctor/doctor";
import {listOffice} from "@/api/office/office";

export default {
  name: "Doctor",
  dicts: ['sys_user_sex'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // doctor表格数据
      doctorList: [],
      // office数据
      // doctor表格数据
      officeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      userIdFlage:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: null,
        gender: null,
        idCard: null,
        phone: null,
        userId: null,
        age: null,
        deptId: null,
        position: null
      },
      // 表单参数
      form: {},
      office:{},

    };
  },
  created() {
    this.handleUpdate();
  },
  methods: {
    getOfficeList(){
      this.loading = true;
      listOffice(this.queryParams).then(response => {
        this.officeList = response.rows;
        this.loading = false;
      });
    },

    // 表单重置
    reset() {
      this.form = {
        patientId: null,
        patientName: null,
        gender: null,
        birthday: null,
        idCard: null,
        phone: null,
        userId: null,
        age: null,
        address: null
      };
      this.resetForm("form");
    },
    /** 更新密钥对按钮操作 */
    upRSA() {
      this.$modal.confirm('是否要更新密钥对？').then(function() {
        return upRSA();
      }).then(() => {
        this.$modal.msgSuccess("更新密钥对成功");
      }).catch(() => {});
    },

    /** 修改按钮操作 */
    handleUpdate() {
      findByPhone().then(response => {
        this.form = response.data;
      });
    }
  }
};
</script>
