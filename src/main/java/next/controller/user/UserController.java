package next.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import next.dao.UserDao;
import next.model.User;

@Controller
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserDao userDao = UserDao.getInstance();
    
	@RequestMapping(value="/users/loginForm", method=RequestMethod.GET)
	public ModelAndView loginForm(){
		return new ModelAndView("/user/login");
	}
	
	@RequestMapping(value="/users/form", method=RequestMethod.GET)
	public ModelAndView joinForm(){
		return new ModelAndView("/user/form");
	}
	
	@RequestMapping(value="/users/create", method=RequestMethod.POST)
	public ModelAndView create(User user) {
        userDao.insert(user);
        log.debug(user + "create user success");
		return new ModelAndView("redirect:/");
	}
}
