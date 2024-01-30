<template>
  <div class="app-container">
    <el-container style="height: 700px; border: 1px solid #eee">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <el-menu :default-openeds="['1']">
          <el-submenu index="1">
            <template slot="title"><i class="el-icon-message"></i>科室选择</template>
            <el-menu-item v-for="(item,key,index) in officeList" :index="'1-'+(key+1)" :key="index" @click="handleClick(item)">
              {{ item.officeName }}
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-container>
        <el-main>
      <el-calendar :range="[getMonday(), getNextSunday()]">
        <template slot="dateCell" slot-scope="{ date, data }">
          <div
            :class="data.isSelected ? 'is-selected' : ''"
            @click="showDoc(data.day)"
          >
            {{ data.day.split('-').slice(1).join('-') }}
          <div style="width: 100%;">
            <el-tag type="success" v-if="isNumber(data.day)">
              有号
            </el-tag>
            <el-tag type="danger" v-else>
              无号
            </el-tag>
            <span v-else>{{ data.day.split('-').slice(1).join('-') }}</span>
          </div>
          </div>
        </template>
      </el-calendar>
        </el-main>

        <el-footer style="min-height: 350px;" >
            <el-button>上午</el-button>
          <el-table v-loading="loading" :data="doctorMsgList" height="130px">
            <el-table-column label="挂号医生姓名" align="center" prop="doctor.username" />
            <el-table-column label="用户简介" align="center" prop="doctor.position" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  :hidden="true"
                  @click="handleAd(scope.row)"
                >挂号</el-button>
              </template>
            </el-table-column>
          </el-table>
            <el-button>下午</el-button>
            <el-table v-loading="loading" :data="doctorAfLsit" height="130px">
              <el-table-column label="挂号医生姓名" align="center" prop="doctor.username" />
              <el-table-column label="用户简介" align="center" prop="doctor.position" />
              <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleAd(scope.row)"
                  :hidden="true"
                >挂号</el-button>
              </template>
              </el-table-column>
            </el-table>
        </el-footer>
      </el-container>
      </el-container>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


    <!-- 添加或修改预约挂号对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="患者姓名" prop="patientName">
          <el-input v-model="form.patientName" placeholder="请输入患者姓名" />
        </el-form-item>
        <el-form-item label="就诊时间" prop="treatmentTime">
          <el-date-picker clearable
                          v-model="form.treatmentTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择就诊时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listRegistration,
  getRegistration,
  delRegistration,
  addRegistration,
  updateRegistration,
  findDocByOffice
} from "@/api/book/registration";
import {listOffice} from "@/api/office/office";

export default {
  name: "Registration",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      accountList:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      hideFlag:false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      footerFlag:true,
      // 预约挂号表格数据
      registrationList: [],
      officeList:[],
      calendarList:[],
      // 弹出层标题
      title: "",
      doctorMsgList:[],
      doctorAfLsit:[],
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patientId: null,
        patientName: null,
        doctorId: null,
        treatmentTime: null,
        doctorName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        patientId: [
          { required: true, message: "患者id不能为空", trigger: "change" }
        ],
        patientName: [
          { required: true, message: "患者姓名不能为空", trigger: "blur" }
        ],
        doctorId: [
          { required: true, message: "挂号医生id不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "挂号时间不能为空", trigger: "blur" }
        ],
        treatmentTime: [
          { required: true, message: "就诊时间不能为空", trigger: "blur" }
        ],
        doctorName: [
          { required: true, message: "挂号医生姓名不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getOfficeList();
  },
  methods: {
    handleAd(row){
      console.log(row)
      this.form.doctorUserId = row.doctor.userId
      this.form.doctorName = row.doctor.username
      this.form.workTime = row.workTime
      this.form.doctorId = row.doctor.id
      this.form.day =  row.day
      console.log(this.form)
      addRegistration(this.form).then(response => {
        this.$modal.msgSuccess("预约成功");
        this.open = false;
        this.getList();
      });
    },
    showDoc(day){
      if (this.isNumber(day)){
        this.hideFlag = true;
      }

      this.doctorAfLsit = this.calendarList.filter(item => item.day === day && item.workTime % 2 === 0)
        .reduce((accumulator, currentValue) => {
          if (!accumulator.some(item => item.docId === currentValue.docId)) {
            accumulator.push(currentValue);
          }
          return accumulator;
        }, []);

      this.doctorMsgList = this.calendarList.filter(item => item.day === day && item.workTime % 2 !== 0)
        .reduce((accumulator, currentValue) => {
          if (!accumulator.some(item => item.docId === currentValue.docId)) {
            accumulator.push(currentValue);
          }
          return accumulator;
        }, []);
      // this.doctorMsgList =Array.from(
      //   new Set(   // 比较日期是否相同
      //     this.calendarList.filter(item=>item.day === day && item.workTime%2 !== 0)
      //     )
      //   );
    },
    isNumber(data){
      const filteredList = this.calendarList.filter(item => {
        return item.day === data; // 比较日期是否相同
      });
      const sum = filteredList.reduce((acc, item) => {
        return acc + item.caseNumer; // 累加每个元素中 number 属性的值
      }, 0);
      return sum > 0;

    },
    handleClick(item){
      findDocByOffice(item).then(response=>{
        this.calendarList = response;
        console.log(this.calendarList)

        const dayList = this.calendarList.map(data=>{
          return data.day;
        })
        this.loading = false;

      })
    },
    getMonday() {
      const today = new Date();
      const day = today.getDay(); // Sunday - Saturday : 0 - 6
      const diffDays = day === 0 ? -6 : 1 - day;
      const monday = new Date(today.getTime() + diffDays * 24 * 60 * 60 * 1000);
      const year = monday.getFullYear();
      const month = monday.getMonth() + 1;
      const date = monday.getDate();
      return year + '-' + month + '-' + date;
    },
    getNextSunday() {
      const today = new Date();
      const day = today.getDay(); // Sunday - Saturday : 0 - 6
      const diffDays = day === 0 ? 7 : 14 - day;
      const sunday = new Date(today.getTime() + diffDays * 24 * 60 * 60 * 1000);
      const year = sunday.getFullYear();
      const month = sunday.getMonth() + 1;
      const date = sunday.getDate();
      return year + '-' + month + '-' + date;
    },
    getOfficeList(){
      this.loading = true;
      listOffice(this.queryParams).then(response => {
        this.officeList = response.rows;
        this.loading = false;
      });
    },
    /** 查询预约挂号列表 */
    getList() {
      this.loading = true;
      listRegistration(this.queryParams).then(response => {
        this.registrationList = response.rows;
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
        registrationId: null,
        patientId: null,
        patientName: null,
        doctorId: null,
        createTime: null,
        treatmentTime: null,
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
      this.ids = selection.map(item => item.registrationId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加预约挂号";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const registrationId = row.registrationId || this.ids
      getRegistration(registrationId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改预约挂号";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.registrationId != null) {
            updateRegistration(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRegistration(this.form).then(response => {
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
      const registrationIds = row.registrationId || this.ids;
      this.$modal.confirm('是否确认删除预约挂号编号为"' + registrationIds + '"的数据项？').then(function() {
        return delRegistration(registrationIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('book/registration/export', {
        ...this.queryParams
      }, `registration_${new Date().getTime()}.xlsx`)
    }
  }
};

</script>

<style>
.scrollable-container {
  width: 1200px;
  height: 100px;
  overflow-y: auto;
}
.scrollable-textarea {
  width: 100%;
  height: 100%;
  resize: none; /* 取消文本框可拖动调整大小的功能 */
  border: none; /* 取消边框样式 */
  padding: 10px; /* 添加一些内边距，让文本内容与容器的边缘有些距离 */
}
</style>
