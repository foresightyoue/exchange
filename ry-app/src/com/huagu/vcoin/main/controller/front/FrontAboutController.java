package com.huagu.vcoin.main.controller.front;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.huagu.vcoin.common.JspPage;
import com.huagu.vcoin.main.controller.BaseController;
import com.huagu.vcoin.main.model.Fabout;
import com.huagu.vcoin.main.service.admin.AboutService;
import com.huagu.vcoin.main.service.front.FrontOthersService;

import net.sf.json.JSONObject;

@Controller
public class FrontAboutController extends BaseController {

    @Autowired
    private FrontOthersService frontOthersService;
    @Autowired
    private AboutService aboutService;

    @RequestMapping("/about/index")
    public ModelAndView index(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int id)
            throws Exception {
        JspPage modelAndView = new JspPage(request);

        Fabout fabout = this.frontOthersService.findFabout(id);

        List<Fabout> abouts = this.aboutService.findAll();

        HashSet<String> ftypes = new HashSet<String>();
        for (Fabout about : abouts) {
            ftypes.add(about.getFtype());
        }

        modelAndView.addObject("ftypes", ftypes);
        modelAndView.addObject("abouts", abouts);
        if (id == 0 && abouts != null) {
            modelAndView.addObject("fabout", abouts.get(0));
        } else {
            modelAndView.addObject("fabout", fabout);
        }
        modelAndView.setJspFile("front/about/index");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/about/index1", produces = BaseController.JsonEncode)
    public String getFabout(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int id) {
        Map<String, Object> map = new HashMap<>();
        Fabout fabout = this.frontOthersService.findFabout(id);
        map.put("fabout", fabout);
        return JSONObject.fromObject(map).toString();
    }

    @RequestMapping("/m/about/index")
    public ModelAndView mAbout(HttpServletRequest request, @RequestParam(required = true) String title) {
        JspPage modelAndView = new JspPage(request);

        List<Fabout> faboutList = this.frontOthersService.findFaboutByProperty("ftitle", title);
        Fabout fabout = null;
        if (faboutList.size() != 0) {
            fabout = faboutList.get(0);
        }

        modelAndView.addObject("fabout", fabout);
        modelAndView.addObject("ftitle", title);
        modelAndView.setJspFile("front/about/index");
        return modelAndView;
    }

    @RequestMapping("/about/wallet")
    public ModelAndView wallet(HttpServletRequest request) throws Exception {
        JspPage modelAndView = new JspPage(request);

        Fabout fabout = this.frontOthersService.findFabout(61);

        String filter = "where ftype='帮助分类'";
        List<Fabout> abouts = this.aboutService.list(0, 0, filter, false);

        modelAndView.addObject("abouts", abouts);
        modelAndView.addObject("fabout", fabout);
        modelAndView.setJspFile("front/about/wallet");
        return modelAndView;
    }

    @RequestMapping("/about/t_detail")
    public ModelAndView t_detail(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int id)
            throws Exception {
        JspPage modelAndView = new JspPage(request);

        Fabout fabout = this.frontOthersService.findFabout(id);
        if (fabout == null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        if (!fabout.getFtype().equals("团队信息")) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        String filter = "where ftype='团队信息'";
        List<Fabout> abouts = this.aboutService.list(0, 0, filter, false);

        modelAndView.addObject("abouts", abouts);
        modelAndView.addObject("fabout", fabout);
        modelAndView.setJspFile("front/about/tdetail");
        return modelAndView;
    }

    @RequestMapping("/about/t_index")
    public ModelAndView t_index(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int id)
            throws Exception {
        JspPage modelAndView = new JspPage(request);

        String filter = "where ftype='团队信息'";
        List<Fabout> abouts = this.aboutService.list(0, 0, filter, false);

        modelAndView.addObject("abouts", abouts);
        modelAndView.setJspFile("front/about/tindex");
        return modelAndView;
    }

    /*
     * @RequestMapping("/dowload/index") public ModelAndView dowload() throws
     * Exception{ ModelAndView modelAndView = new ModelAndView() ;
     * 
     * modelAndView.setViewName("front"+Mobilutils.M(request)+"/dowload/index")
     * ; return modelAndView ; }
     * 
     * @RequestMapping("/business") public ModelAndView business() throws
     * Exception{ ModelAndView modelAndView = new ModelAndView() ; int isIndex =
     * 1; modelAndView.addObject("isIndex", isIndex) ;
     * modelAndView.setViewName("front"+Mobilutils.M(request)+"/about/business")
     * ; return modelAndView ; }
     */
}
