package com.zhangyang.controller;

import com.zhangyang.pojo.Knowledge;
import com.zhangyang.pojo.KnowledgeForAdminView;
import com.zhangyang.service.impl.CodeQuestionService;
import com.zhangyang.service.impl.ExamService;
import com.zhangyang.service.impl.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张杨
 * @Date
 */
@Controller
public class KnowledgeController {
    @Autowired
    KnowledgeService knowledgeService;
    @Autowired
    ExamService examService;
    @Autowired
    CodeQuestionService codeQuestionService;

    @GetMapping("/adminForKnowledgeQuery")
    public ModelAndView adminForKnowledgeQuery(){
        ModelAndView mav=new ModelAndView();
        //查询所有的知识点
        List<Knowledge> allKnowledge = knowledgeService.getAll();
        //对查询的数据重新封装
        List<KnowledgeForAdminView> knowledgeForAdminViewList=new ArrayList<>();
        for (Knowledge knowledge :
                allKnowledge) {
            KnowledgeForAdminView knowledgeForAdminView = new KnowledgeForAdminView();
            knowledgeForAdminView.setId(knowledge.getId());
            knowledgeForAdminView.setKnowledgename(knowledge.getKnowledgename());
            //根据knowledgeid去查询该知识点的习题数
            int examCount=examService.getExamCountByKnowledgeid(knowledge.getId());
            int codeQuestionCount=codeQuestionService.getQuestionCountByKnowledgeid(knowledge.getId());
            knowledgeForAdminView.setCount(examCount+codeQuestionCount);
            knowledgeForAdminViewList.add(knowledgeForAdminView);
        }
        mav.addObject("knowledgeForAdminViewList",knowledgeForAdminViewList);
        mav.setViewName("adminForKnowledgeQuery");
        return mav;

    }
    @GetMapping("/adminForKnowledgeInsert")
    public ModelAndView adminForKnowledgeInsert(){
        ModelAndView mav=new ModelAndView();
        mav.setViewName("adminForKnowledgeInsert");
        return mav;
    }
    @PostMapping("/knowledgeInsert")
    public ModelAndView knowledgeInsert(@RequestParam("knowledgename")String knowledgename){
        ModelAndView mav=new ModelAndView();
        //判断这个knowledge是否已经存在
        Knowledge knowledgeBySearch=knowledgeService.getByKnowledgeName(knowledgename);
        if(knowledgeBySearch!=null){
            //查询到了
            mav.addObject("error","该知识点已经存在！");
            mav.setViewName("adminForKnowledgeInsert");
            return mav;
        }
        //插入一条记录
        Knowledge knowledge=new Knowledge();
        knowledge.setKnowledgename(knowledgename);
        int i=knowledgeService.addKnowledge(knowledge);
        mav.setViewName("redirect:/adminForKnowledgeInsert");
        return mav;

    }

    @GetMapping("/adminForKnowledgeUpdate")
    public ModelAndView adminForKnowledgeUpdate(@RequestParam("id")Integer id){
        ModelAndView mav=new ModelAndView();
        //根据id查询信息
        Knowledge knowledge = knowledgeService.getByPrimaryKey(id);
        mav.addObject("knowledge",knowledge);
        mav.setViewName("adminForKnowledgeUpdate");
        return mav;
    }

    @PostMapping("/knowledgeUpdate")
    public ModelAndView knowledgeUpdate(@RequestParam("id")Integer id,@RequestParam("knowledgename")String knowledgename){
        ModelAndView mav=new ModelAndView();
        //判断这个knowledge是否已经存在
        Knowledge knowledgeBySearch=knowledgeService.getByKnowledgeName(knowledgename);
        if(knowledgeBySearch!=null){
            //查询到了
            mav.addObject("error","该知识点已经存在！");
            mav.setViewName("adminForKnowledgeInsert");
            return mav;
        }
        //更新一条记录
        Knowledge knowledge=new Knowledge();
        knowledge.setKnowledgename(knowledgename);
        knowledge.setId(id);
        int i=knowledgeService.updateKnowledge(knowledge);
        mav.setViewName("redirect:/adminForKnowledgeUpdate?id="+id);
        return mav;

    }

    @GetMapping("/knowledgeDeleteByAdmin")
    public  ModelAndView knowledgeDeleteByAdmin(@RequestParam("id")Integer id){
        ModelAndView mav=new ModelAndView();
        //删除一条记录
        int i=knowledgeService.removeByPrimaryKey(id);
        //重定向到查询页面
        mav.setViewName("redirect:/adminForKnowledgeQuery");
        return mav;
    }

}
