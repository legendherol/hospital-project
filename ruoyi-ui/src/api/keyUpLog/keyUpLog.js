import request from '@/utils/request'

// 查询密钥修改日志列表
export function listKeyUpLog(query) {
  return request({
    url: '/keyUpLog/keyUpLog/list',
    method: 'get',
    params: query
  })
}

// 查询密钥修改日志详细
export function getKeyUpLog(id) {
  return request({
    url: '/keyUpLog/keyUpLog/' + id,
    method: 'get'
  })
}

// 新增密钥修改日志
export function addKeyUpLog(data) {
  return request({
    url: '/keyUpLog/keyUpLog',
    method: 'post',
    data: data
  })
}

// 修改密钥修改日志
export function updateKeyUpLog(data) {
  return request({
    url: '/keyUpLog/keyUpLog',
    method: 'put',
    data: data
  })
}

// 删除密钥修改日志
export function delKeyUpLog(id) {
  return request({
    url: '/keyUpLog/keyUpLog/' + id,
    method: 'delete'
  })
}
