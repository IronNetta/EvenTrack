package org.seba.eventrack.api.models.mails.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailsDTO {

        private String recipient;
        private String msgBody;
        private String subject;
        private String attachment;

        public EmailsDTO(String email, String msgBody, String subject) {
                this.recipient = email;
                this.msgBody = msgBody;
                this.subject = subject;
        }
}
