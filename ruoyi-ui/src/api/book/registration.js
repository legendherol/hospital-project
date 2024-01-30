import request from '@/utils/request'

// 查询预约挂号列表
export function listRegistration(query) {
  return request({
    url: '/book/registration/list',
    method: 'get',
    params: query
  })
}

// 查询预约挂号详细
export function getRegistration(registrationId) {
  return request({
    url: '/book/registration/' + registrationId,
    method: 'get'
  })
}

// 新增预约挂号
export function addRegistration(data) {
  return request({
    url: '/book/registration',
    method: 'post',
    data: data
  })
}

// 修改预约挂号
export function updateRegistration(data) {
  return request({
    url: '/book/registration',
    method: 'put',
    data: data
  })
}

// 删除预约挂号
export function delRegistration(registrationId) {
  return request({
    url: '/book/registration/' + registrationId,
    method: 'delete'
  })
}

// 发现医生的剩余挂号数
export function findDocByOffice(data) {
  return request({
    url: '/book/registration/doc',
    method: 'post',
    data: data
    // 使用表单传参大致是需要设置headers的
    // headers: {
    //   'Content-Type': 'application/x-www-form-urlencoded'
    // }
  })
}

// 查询预约挂号列表
export function listNowBook(query) {
  return request({
    url: '/book/registration/listNow',
    method: 'get',
    params: query
  })
}
