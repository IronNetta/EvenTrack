/*package org.seba.eventrack.api.controllers.mails;

import org.seba.eventrack.api.models.mails.dtos.EmailsDTO;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.seba.eventrack.bll.services.mails.EmailService;
import org.seba.eventrack.api.models.mails.dtos.EmailsDTO;

@RestController
public class EmailController {


    @Autowired private EmailService emailService;

    // Sending a simple Email
    @PostMapping("/sendMail")
    public String
    sendMail(@RequestBody EmailsDTO details)
    {
        String status
                = emailService.sendSimpleMail(details);

        return status;
    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
            @RequestBody EmailsDTO details)
    {
        String status
                = emailService.sendMailWithAttachment(details);

        return status;
    }

}*/
