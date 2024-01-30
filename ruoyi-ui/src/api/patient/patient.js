import request from '@/utils/request'

// 查询患者管理列表
export function listPatient(query) {
  return request({
    url: '/patient/patient/list',
    method: 'get',
    params: query
  })
}

// 查询患者管理详细
export function getPatient(patientId) {
  return request({
    url: '/patient/patient/' + patientId,
    method: 'get'
  })
}

// 新增患者管理
export function addPatient(data) {
  return request({
    url: '/patient/patient',
    method: 'post',
    data: data
  })
}

// 修改患者管理
export function updatePatient(data) {
  return request({
    url: '/patient/patient/put',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'application/json' },

  })
}

// 删除患者管理
export function delPatient(patientId) {
  return request({
    url: '/patient/patient/' + patientId,
    method: 'delete'
  })
}

// 查询患者管理详细
export function findByPhone() {
  return request({
    url: '/patient/patient/find',
    method: 'get'
  })
}

// 查询患者管理详细
export function upRSA() {
  return request({
    url: '/patient/patient/updateRsa',
    method: 'get'
  })
}
