package app.invoice.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Data
public class ChangeUserPasswordForm {

    @NotEmpty(message = "ChangeUserPasswordForm username is blank")
    @NotNull(message = "ChangeUserPasswordForm username is blank")
    @Size(max = 50, message = "ChangeUserPasswordForm userName max size 50")
    String userName;

    @NotNull(message = "ChangeUserPasswordForm old password is blank")
    String oldPassword;

//    @NotEmpty(message = "ChangeUserPasswordForm new password is blank")
//    @NotNull(message = "ChangeUserPasswordForm new password is blank")
    @Size(min = 5, message = "ChangeUserPasswordForm new password min size 5")
    String newPassword;

//    @NotEmpty(message = "hangeUserPasswordForm confirm password is blank")
//    @NotNull(message = "ChangeUserPasswordForm confirm password is blank")
    String confirmNewPassword;
}
