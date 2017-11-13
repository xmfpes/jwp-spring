package next.controller.user;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import next.controller.UserSessionUtils;
import next.dao.UserDao;
import next.model.User;

@Controller
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private UserDao userDao = UserDao.getInstance();

	@RequestMapping(value = "/users/update", method = RequestMethod.POST)
	public ModelAndView update(String userId, User user, HttpSession session) {
		if (!UserSessionUtils.isSameUser(session, user)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		User updateUser = user;
		log.debug("Update User : {}", updateUser);
		user.update(updateUser);
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/users/updateForm", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpSession session, String userId) {
		User user = userDao.findByUserId(userId);

		if (!UserSessionUtils.isSameUser(session, user)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		ModelAndView mav = new ModelAndView("/user/updateForm");
		mav.addObject("user", user);
		return mav;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ModelAndView showUserList(HttpSession session) throws SQLException {
		if (!UserSessionUtils.isLogined(session)) {
			return new ModelAndView("redirect:/users/loginForm");
		}

		ModelAndView mav = new ModelAndView("/user/list");
		mav.addObject("users", userDao.findAll());
		return mav;
	}

	@RequestMapping(value = "/users/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session) {
		session.removeAttribute("user");
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/users/login", method = RequestMethod.POST)
	public ModelAndView login(String userId, String password, HttpSession session) {
		log.debug("login process");
		User user = userDao.findByUserId(userId);
		if (user == null) {
			throw new NullPointerException("사용자를 찾을 수 없습니다.");
		}

		if (user.matchPassword(password)) {
			session.setAttribute("user", user);
			log.debug("login success");
			return new ModelAndView("redirect:/");
		} else {
			throw new IllegalStateException("비밀번호가 틀립니다.");
		}
	}

	@RequestMapping(value = "/users/profile", method = RequestMethod.GET)
	public ModelAndView showProfile(String userId) {
		ModelAndView mav = new ModelAndView("/user/profile.jsp");
		mav.addObject("user", userDao.findByUserId(userId));
		return mav;
	}

	@RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
	public ModelAndView loginForm() {
		return new ModelAndView("/user/login");
	}

	@RequestMapping(value = "/users/form", method = RequestMethod.GET)
	public ModelAndView joinForm() {
		return new ModelAndView("/user/form");
	}

	@RequestMapping(value = "/users/create", method = RequestMethod.POST)
	public ModelAndView create(User user) {
		userDao.insert(user);
		log.debug(user + "create user success");
		return new ModelAndView("redirect:/");
	}
}
