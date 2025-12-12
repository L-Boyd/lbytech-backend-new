package com.lbytech.lbytech_backend_new.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbytech.lbytech_backend_new.constant.ThumbConstant;
import com.lbytech.lbytech_backend_new.exception.BusinessException;
import com.lbytech.lbytech_backend_new.mapper.ThumbRecordMapper;
import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import com.lbytech.lbytech_backend_new.pojo.entity.Notebook;
import com.lbytech.lbytech_backend_new.pojo.entity.ThumbRecord;
import com.lbytech.lbytech_backend_new.pojo.vo.UserVO;
import com.lbytech.lbytech_backend_new.service.INotebookService;
import com.lbytech.lbytech_backend_new.service.IThumbRecordService;
import com.lbytech.lbytech_backend_new.util.RedisKeyUtil;
import com.lbytech.lbytech_backend_new.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class ThumbRecordServiceImpl extends ServiceImpl<ThumbRecordMapper, ThumbRecord> implements IThumbRecordService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    @Lazy // 懒加载，解决循环依赖
    private INotebookService notebookService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Boolean thumbNotebook(Integer notebookId) {
        UserVO user = UserHolder.getUser();

        // 加锁
        synchronized (user.getEmail().intern()) {
            // 事务式编程
            Boolean executed = transactionTemplate.execute(status -> {
                // 检查是否已点赞
                /*boolean exists = this.lambdaQuery()
                        .eq(ThumbRecord::getUserEmail, user.getEmail())
                        .eq(ThumbRecord::getNotebookId, notebookId)
                        .exists();*/
                boolean exists = hasThumb(user.getEmail(), notebookId);
                if (exists) {
                    throw new BusinessException(StatusCodeEnum.FAIL, "用户已点赞该笔记");
                }

                boolean update = notebookService.lambdaUpdate()
                        .eq(Notebook::getId, notebookId)
                        .setSql("thumb_count = thumb_count + 1")
                        .update();

                ThumbRecord thumbRecord = new ThumbRecord();
                thumbRecord.setNotebookId(notebookId);
                thumbRecord.setUserEmail(user.getEmail());

                boolean success = update && this.save(thumbRecord);

                if (success) {
                    // 点赞记录存入redis：key为用户邮箱，field为笔记id，value为点赞记录id
                    stringRedisTemplate.opsForHash().put(RedisKeyUtil.getUserThumbKey(user.getEmail()), notebookId.toString(), thumbRecord.getId().toString());
                }

                // 两个都成功才执行
                return success;
            });

            return executed;
        }
    }

    @Override
    public Boolean unThumbNotebook(Integer notebookId) {
        UserVO user = UserHolder.getUser();

        // 加锁
        synchronized (user.getEmail().intern()) {
            // 事务式编程
            Boolean executed = transactionTemplate.execute(status -> {
                // 检查是否已点赞
                /*ThumbRecord thumbRecord = this.lambdaQuery()
                        .eq(ThumbRecord::getUserEmail, user.getEmail())
                        .eq(ThumbRecord::getNotebookId, notebookId)
                        .one();*/
                // 从redis中获取点赞记录id
                Object thumbRecordIdObj = stringRedisTemplate.opsForHash().get(ThumbConstant.USER_THUMB_KEY_PREFIX + user.getEmail(), notebookId.toString());
                if (thumbRecordIdObj == null) {
                    throw new BusinessException(StatusCodeEnum.FAIL, "用户未点赞该笔记");
                }
                Long thumbRecordId = Long.valueOf(thumbRecordIdObj.toString());

                boolean update = notebookService.lambdaUpdate()
                        .eq(Notebook::getId, notebookId)
                        .setSql("thumb_count = thumb_count - 1")
                        .update();

                // 两个都成功才执行
                boolean success = update && this.removeById(thumbRecordId);

                if (success) {
                    // 从redis中删除点赞记录id
                    stringRedisTemplate.opsForHash().delete(ThumbConstant.USER_THUMB_KEY_PREFIX + user.getEmail(), notebookId.toString());
                }

                return success;
            });

            return executed;
        }
    }

    @Override
    public Boolean hasThumb(String userEmail, Integer notebookId) {
        return stringRedisTemplate.opsForHash().hasKey(ThumbConstant.USER_THUMB_KEY_PREFIX + userEmail, notebookId.toString());
    }
}
