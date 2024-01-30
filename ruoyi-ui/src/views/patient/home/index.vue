<template>
  <div class="app-container">
    <!-- 添加或修改patient对话框 -->
      <el-form ref="form" :model="form"  label-width="80px" >
        <el-form-item label="患者名字" prop="patientName" :disabled="true">
          <el-input v-model="form.patientName" placeholder="请输入患者名字" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别">
            <el-option
              v-for="dict in dict.type.sys_user_sex"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
              :disabled="true"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday" :disabled="true">
          <el-input v-model="form.birthday" placeholder="请选择出生日期" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard" :disabled="true">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="电话号码" prop="phone" :disabled="true">
          <el-input v-model="form.phone" placeholder="请输入电话号码" />
        </el-form-item>
        <el-form-item label="用户id" prop="userId" :disabled="true">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="年龄" prop="age" :disabled="true">
          <el-input v-model="form.age" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="地址" prop="address" :disabled="true">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
      </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="upRSA">更新密钥对</el-button>
    </div>
  </div>
</template>

<script>
import {findByPhone, upRSA} from "@/api/patient/patient";

export default {
  name: "Patient",
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
      // patient表格数据
      patientList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patientName: null,
        gender: null,
        birthday: null,
        idCard: null,
        phone: null,
        userId: null,
        age: null,
        address: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        patientName: [
          { required: true, message: "患者名字不能为空", trigger: "blur" }
        ],
        gender: [
          { required: true, message: "性别不能为空", trigger: "blur" }
        ],
        birthday: [
          { required: true, message: "出生日期不能为空", trigger: "blur" }
        ],
        idCard: [
          { required: true, message: "身份证号不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "电话号码不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" }
        ],
        age: [
          { required: true, message: "年龄不能为空", trigger: "blur" }
        ],
        address: [
          { required: true, message: "地址不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.handleUpdate();
  },
  methods: {
    /** 更新密钥对按钮操作 */
    upRSA() {
      this.$modal.confirm('是否要更新密钥对？').then(function() {
        upRSA();
      }).then(() => {
        this.$modal.msgSuccess("更新密钥对成功");
      }).catch(() => {});
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

    /** 修改按钮操作 */
    handleUpdate() {
      findByPhone().then(response => {
        this.form = response.data;
      });
    }



  }
};
</script>
