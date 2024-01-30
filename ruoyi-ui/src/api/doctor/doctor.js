import request from '@/utils/request'

// 查询doctor列表
export function listDoctor(query) {
  return request({
    url: '/doctor/doctor/list',
    method: 'get',
    params: query
  })
}

// 查询doctor详细
export function getDoctor(id) {
  return request({
    url: '/doctor/doctor/' + id,
    method: 'get'
  })
}

// 新增doctor
export function addDoctor(data) {
  return request({
    url: '/doctor/doctor',
    method: 'post',
    data: data
  })
}

// 修改doctor
export function updateDoctor(data) {
  return request({
    url: '/doctor/doctor',
    method: 'put',
    data: data
  })
}

// 删除doctor
export function delDoctor(id) {
  return request({
    url: '/doctor/doctor/' + id,
    method: 'delete'
  })
}

// 查询患者管理详细
export function findByPhone() {
  return request({
    url: '/doctor/doctor/find',
    method: 'get'
  })
}

// 查询患者管理详细
export function upRSA() {
  return request({
    url: '/doctor/doctor/updateRsa',
    method: 'get'
  })
}
