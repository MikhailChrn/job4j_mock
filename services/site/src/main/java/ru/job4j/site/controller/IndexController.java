package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.dto.ProfileDTO;
import ru.job4j.site.service.*;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

/**
 * Добавил приватный метод для заполнения 'String submitterName'
 * в 'List<InterviewDTO>'
 */

@Controller
@AllArgsConstructor
@Slf4j
public class IndexController {
    private final CategoriesService categoriesService;
    private final InterviewsService interviewsService;
    private final AuthService authService;
    private final NotificationService notifications;
    private final ProfilesService profilesService;

    @GetMapping({"/", "index"})
    public String getIndexPage(Model model, HttpServletRequest req) throws JsonProcessingException {
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/"
        );
        try {
            model.addAttribute("categories", categoriesService.getMostPopular());
            var token = getToken(req);
            if (token != null) {
                var userInfo = authService.userInfo(token);
                model.addAttribute("userInfo", userInfo);
                model.addAttribute("userDTO", notifications.findCategoriesByUserId(userInfo.getId()));
                RequestResponseTools.addAttrCanManage(model, userInfo);
            }
        } catch (Exception e) {
            log.error("Remote application not responding. Error: {}. {}, ", e.getCause(), e.getMessage());
        }

        List<InterviewDTO> interviewDTOList = addSubmitterNameIntoInterviewDTOs(interviewsService.getByType(1));

        model.addAttribute("new_interviews", interviewDTOList);

        return "index";
    }

    private List<InterviewDTO> addSubmitterNameIntoInterviewDTOs(List<InterviewDTO> interviewDTOList) {
        return interviewDTOList.stream()
                .filter(Objects::nonNull)
                .peek(interviewDTO -> {
                    Optional<ProfileDTO> profileOpt = profilesService.getProfileById(interviewDTO.getSubmitterId());
                    String username = profileOpt
                            .map(ProfileDTO::getUsername)
                            .orElse("<Unknown>");
                    interviewDTO.setSubmitterName(username);
                })
                .collect(Collectors.toList());
    }
}