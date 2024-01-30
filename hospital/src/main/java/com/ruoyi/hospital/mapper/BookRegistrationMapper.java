package com.ruoyi.hospital.mapper;

import java.util.List;
import com.ruoyi.hospital.domain.BookRegistration;
import com.ruoyi.hospital.domain.Doctor;
import com.ruoyi.hospital.domain.Office;
import com.ruoyi.hospital.domain.WorkTime;
import org.apache.ibatis.annotations.Param;

/**
 * 预约挂号Mapper接口
 * 
 * @author liu
 * @date 2023-06-02
 */
public interface BookRegistrationMapper 
{
    /**
     * 查询预约挂号
     * 
     * @param registrationId 预约挂号主键
     * @return 预约挂号
     */
    public BookRegistration selectBookRegistrationByRegistrationId(Long registrationId);

    /**
     * 查询预约挂号列表
     * 
     * @param bookRegistration 预约挂号
     * @return 预约挂号集合
     */
    public List<BookRegistration> selectBookRegistrationList(BookRegistration bookRegistration);

    /**
     * 新增预约挂号
     * 
     * @param bookRegistration 预约挂号
     * @return 结果
     */
    public int insertBookRegistration(BookRegistration bookRegistration);

    /**
     * 修改预约挂号
     * 
     * @param bookRegistration 预约挂号
     * @return 结果
     */
    public int updateBookRegistration(BookRegistration bookRegistration);

    /**
     * 删除预约挂号
     * 
     * @param registrationId 预约挂号主键
     * @return 结果
     */
    public int deleteBookRegistrationByRegistrationId(Long registrationId);

    /**
     * 批量删除预约挂号
     * 
     * @param registrationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBookRegistrationByRegistrationIds(Long[] registrationIds);

    List<WorkTime> getWorkTime(Office office);

    List<BookRegistration> getBookList(Long userId);

    BookRegistration selectPatientByTreat(String treatmentNum);

}
