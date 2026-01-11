package com.lbytech.lbytech_backend_new.es;

import com.lbytech.lbytech_backend_new.pojo.vo.BaseResponse;
import com.lbytech.lbytech_backend_new.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Elasticsearch前端控制器
 */
@RestController
@RequestMapping("/es/notebook")
public class NotebookEsController {

    @Autowired
    private INotebookEsService notebookEsService;

    /**
     * 根据文件名模糊查询笔记
     * @param fileName 文件名关键词
     * @return 笔记列表
     */
    @GetMapping("/getNotebookByFileNameContaining")
    public BaseResponse<List<NotebookForEsVO>> getNotebookByFileNameContaining(@RequestParam String fileName) {
        List<NotebookForEsVO> notebookForEsVOS = notebookEsService.getNotebookByFileNameContaining(fileName);
        return ResultUtil.success(notebookForEsVOS);
    }
}