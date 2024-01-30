import request from '@/utils/request'

// 查询门诊列表
export function listCase(query) {
  return request({
    url: '/case/case/list',
    method: 'get',
    params: query
  })
}

// 查询门诊详细
export function getCase(caseHistoryId) {
  return request({
    url: '/case/case/' + caseHistoryId,
    method: 'get'
  })
}

// 新增门诊
export function addCase(data) {
  return request({
    url: '/case/case',
    method: 'post',
    data: data
  })
}

// 修改门诊
export function updateCase(data) {
  return request({
    url: '/case/case',
    method: 'put',
    data: data
  })
}

// 删除门诊
export function delCase(caseHistoryId) {
  return request({
    url: '/case/case/' + caseHistoryId,
    method: 'delete'
  })
}

// 查询门诊详细
export function getBookPatient(registrationId) {
  return request({
    url: '/book/registration/' + registrationId,
    method: 'get'
  })
}

/**
 * 挂号患者
 * @returns {AxiosPromise}
 */
export function listBookPatient(){
  return request({
    url: '/case/case/bookPatient',
    method: 'get',
  })
}

// 查询门诊列表
export function listCasePatient(query) {
  return request({
    url: '/case/case/listNow',
    method: 'get',
    params: query
  })
}

// 查询门诊列表
export function listDocCase(query) {
  return request({
    url: '/case/case/listDoc',
    method: 'get',
    params: query
  })
}

