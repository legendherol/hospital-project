<template>
  <div class="app-container">
    <!--    <el-button type="text" @click="table = true">打开嵌套表格的 Drawer</el-button>-->
    <el-button @click="clickButn" type="primary" style="margin-left: 16px;">
      点我打开需要诊断的患者
    </el-button>
    <el-drawer
      title="诊断患者"
      :visible.sync="table"
      direction="rtl"
      size="50%">
      <p style="color: #ff0000">未诊患者</p>
      <el-table :data="gridData" title="未诊患者" height="350px" @row-click="handleRowClick">
        <el-table-column property="treatmentNum" label="就诊号" width="150"></el-table-column>
        <el-table-column property="patientName" label="名字" width="200"></el-table-column>
        <el-table-column property="patient.age" label="年龄"></el-table-column>
      </el-table>
      <p style="color: grey">已诊患者</p>
      <el-table :data="treatList" title="已诊患者" height="350px">
        <el-table-column property="treatmentNum" label="就诊号" width="150"></el-table-column>
        <el-table-column property="patientName" label="名字" width="200"></el-table-column>
        <el-table-column property="patient.age" label="年龄"></el-table-column>
      </el-table>
    </el-drawer>

    <!--    <el-row :gutter="10" class="mb8">-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="primary"-->
    <!--          plain-->
    <!--          icon="el-icon-plus"-->
    <!--          size="mini"-->
    <!--          @click="handleAdd"-->
    <!--          v-hasPermi="['case:case:add']"-->
    <!--        >新增</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="success"-->
    <!--          plain-->
    <!--          icon="el-icon-edit"-->
    <!--          size="mini"-->
    <!--          :disabled="single"-->
    <!--          @click="handleUpdate"-->
    <!--          v-hasPermi="['case:case:edit']"-->
    <!--        >修改</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="danger"-->
    <!--          plain-->
    <!--          icon="el-icon-delete"-->
    <!--          size="mini"-->
    <!--          :disabled="multiple"-->
    <!--          @click="handleDelete"-->
    <!--          v-hasPermi="['case:case:remove']"-->
    <!--        >删除</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="warning"-->
    <!--          plain-->
    <!--          icon="el-icon-download"-->
    <!--          size="mini"-->
    <!--          @click="handleExport"-->
    <!--          v-hasPermi="['case:case:export']"-->
    <!--        >导出</el-button>-->
    <!--      </el-col>-->
    <!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
    <!--    </el-row>-->


    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改门诊对话框 -->
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item type="textarea" autosize label="就诊号" prop="treatmentNum" >
        <el-input v-model="form.treatmentNum" placeholder="请输入就诊号" :disabled="true"/>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model="form.age" placeholder="请输入年龄" :disabled="true"/>
      </el-form-item>
      <el-form-item label="患者姓名" prop="patientName">
        <el-input type="textarea" autosize v-model="form.patientName" placeholder="请输入患者姓名" :disabled="true"/>
      </el-form-item>
<!--      <el-form-item label="诊断医生id" prop="createUserId">-->
<!--        <el-input type="textarea" autosize v-model="form.createUserId" placeholder="请输入诊断医生id"/>-->
<!--      </el-form-item>-->
      <el-form-item label="主诉" prop="chiefComplaint">
        <el-input type="textarea" autosize v-model="form.chiefComplaint" placeholder="请输入主诉"/>
      </el-form-item>
<!--      <el-form-item label="医生用户id" prop="userId">-->
<!--        <el-input type="textarea" autosize v-model="form.userId" placeholder="请输入医生用户id"/>-->
<!--      </el-form-item>-->
      <el-form-item label="既往史" prop="previousHistory">
        <el-input type="textarea" autosize v-model="form.previousHistory" placeholder="请输入既往史"/>
      </el-form-item>
      <el-form-item label="过敏史" prop="allergyHistory">
        <el-input type="textarea" autosize v-model="form.allergyHistory" placeholder="请输入过敏史"/>
      </el-form-item>
      <el-form-item label="诊断结果" prop="caseResult">
        <el-input type="textarea" autosize v-model="form.caseResult" placeholder="请输入诊断结果"/>
      </el-form-item>
      <el-form-item label="诊断时间" prop="caseTime">
        <el-date-picker clearable
                        v-model="form.caseTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择诊断时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="处方" prop="prescriptionDrug">
        <el-input type="textarea" autosize v-model="form.prescriptionDrug" placeholder="请输入处方"/>
      </el-form-item>
      <el-form-item label="医嘱" prop="caseAdvice">
        <el-input type="textarea" autosize v-model="form.caseAdvice" placeholder="请输入医嘱"/>
      </el-form-item>

      <el-form-item label="医生姓名" prop="doctorName">
        <el-input type="textarea" autosize v-model="form.doctorName" placeholder="请输入医生姓名" :disabled="true"/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="treat">诊 断</el-button>
    </div>
  </div>
</template>

<script>
import {listCase, getCase, delCase, addCase, updateCase, getBookPatient, listBookPatient} from "@/api/case/case";
import store from "@/store";
import {encrypt, getPrivateKey, md5Encry, privencrypt, testEncry} from "@/api/aesEncry/aesEncry";

export default {
  name: "Case",
  data() {
    return {
      // 遮罩层
      drawerFlag: true,
      // 已诊断List
      treatList: [],
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
      // 门诊表格数据
      caseList: [],
      gridData: [],
      // 弹出层标题
      title: "",
      table: false,
      serverPrivateKey: "",
      aesTransferKey: "",
      // 是否显示弹出层
      open: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patientId: null,
        createUserId: null,
        chiefComplaint: null,
        userId: null,
        previousHistory: null,
        allergyHistory: null,
        caseResult: null,
        caseTime: null,
        prescriptionDrug: null,
        caseAdvice: null,
        treatmentNum: null,
        patientName: null,
        doctorName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        patientId: [
          {required: true, message: "患者id不能为空", trigger: "blur"}
        ],
        createUserId: [
          {required: true, message: "诊断医生id不能为空", trigger: "blur"}
        ],
        chiefComplaint: [
          {required: true, message: "主诉不能为空", trigger: "blur"}
        ],
        userId: [
          {required: true, message: "医生用户id不能为空", trigger: "blur"}
        ],
        previousHistory: [
          {required: true, message: "既往史不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getPrivateKey()
    // this.testAes();
    // this.getList();
    this.getGridData();
  },
  methods: {
    testAes() {
      const key = "O8MEO1Wsq9yLKu0N";
      const a = encrypt("123", key);
      console.log(a);
    },
    getPrivateKey() {
      getPrivateKey().then(result => {
        // console.log(""+result.data)
        // console.log(""+result)
        this.serverPrivateKey = result.userPrivateKey;
        this.aesTransferKey = result.aesTransferKey;
      })
    },
    treat() {

      this.$refs["form"].validate(valid => {
        if (valid) {
          // testEncry()

          this.aesTransferKey="O8MEO1Wsq9yLKu0N";
          addCase(encrypt(privencrypt(md5Encry(this.form), this.serverPrivateKey)+'|'+JSON.stringify(this.form), this.aesTransferKey)).then(response => {

            this.$modal.msgSuccess("诊断成功");
            this.$modal.msgSuccess("数据签名验证成功");
            // 给他推进到另外一个表
            const obj = this.gridData.find(data => {
              return data.treatmentNum === this.form.treatmentNum;
            })

            this.gridData = this.gridData.filter(data => {
              return data.treatmentNum !== this.form.treatmentNum;
            })

            // obj.showFlag = true;
            this.treatList.push(obj);
            this.reset();
          });
        }

      });

    },
    getGridData() {
      listBookPatient().then(response => {
        this.gridData = response;
        this.loading = false;
      });
    },
    clickButn() {
      this.table = true;
    },
    handleRowClick(row) {
      console.log(row)
      this.form.patientName = row.patientName;
      this.form.age = row.patient.age;
      this.form.patientId = row.patientId;
      this.form.treatmentNum = row.treatmentNum;
      this.form.doctorName = row.doctorName;
      this.form.createUserId = row.doctorId;
      const registrationId = row.registrationId || this.ids;
      // getBookPatient(registrationId).then(response => {
      //   // this.form.treatmentNum = response.data.treatmentNum;
      //   // console.log(response.data)
      //   // this.form.patientId = row.patientId
      //   // this.form.patienName = response.data.patienName;
      //   // this.form.age = row.age;
      // });
      this.table = false;
      console.log(this.form)
    },
    /** 查询门诊列表 */
    getList() {
      this.loading = true;
      listCase(this.queryParams).then(response => {
        this.caseList = response.rows;
        console.log(this.caseList)
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        caseHistoryId: null,
        patientId: null,
        createUserId: null,
        chiefComplaint: null,
        userId: null,
        previousHistory: null,
        allergyHistory: null,
        caseResult: null,
        caseTime: null,
        prescriptionDrug: null,
        caseAdvice: null,
        patientName: null,
        doctorName: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.caseHistoryId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.open = true;
      this.title = "添加门诊";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const caseHistoryId = row.caseHistoryId || this.ids
      getCase(caseHistoryId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改门诊";
      });
    },
    /** 提交按钮 */
    submitForm() {

      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.caseHistoryId != null) {
            updateCase(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCase(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const caseHistoryIds = row.caseHistoryId || this.ids;
      this.$modal.confirm('是否确认删除门诊编号为"' + caseHistoryIds + '"的数据项？').then(function () {
        return delCase(caseHistoryIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('case/case/export', {
        ...this.queryParams
      }, `case_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
