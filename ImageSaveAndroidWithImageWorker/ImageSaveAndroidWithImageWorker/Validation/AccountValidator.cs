using FluentValidation;
using ImageSaveAndroidWithImageWorker.Data.Identity.Entities;
using ImageSaveAndroidWithImageWorker.Models;
using Microsoft.AspNetCore.Identity;

namespace ImageSaveAndroidWithImageWorker.Validation
{
    public class RegisterValidator : AbstractValidator<RegisterUserModel>
    {
        private UserManager<AppUser> _userManager { get; set; }
       
        public RegisterValidator(UserManager<AppUser> userManager)
        {
            _userManager = userManager;
            RuleFor(x => x.Email)
                .NotEmpty().WithMessage("Поле не може бути пустим!")
                .EmailAddress().WithMessage("Не коректно вказано електронна пошта!")
                .Must(IsUniqueEmail).WithMessage("Аккаунт вже зареєстрований!");

            RuleFor(x => x.FirstName)
                .NotEmpty().WithMessage("Поле не може бути пустим!")
                .MinimumLength(2).WithMessage("Мінімальна кількість символів - 2");

            RuleFor(x => x.SecondName)
                .NotEmpty().WithMessage("Поле не може бути пустим!")
                .MinimumLength(2).WithMessage("Мінімальна кількість символів - 2");

            RuleFor(x => x.Phone)
                .NotEmpty().WithMessage("Поле не може бути пустим!")
                .MaximumLength(20).WithMessage("Максимальна кількість символів - 20")
                .MinimumLength(10).WithMessage("Мінімальна кількість символів - 10");

            RuleFor(x => x.Password)
                .NotEmpty().WithMessage("Поле не може бути пустим!")
                .MinimumLength(5).WithMessage("Мінімальна кількість символів - 5");

            RuleFor(x => x.ConfirmPassword)
                .NotEmpty().WithMessage("Поле не може бути пустим!")
                .MinimumLength(5).WithMessage("Мінімальна кількість символів - 5")
                .Equal(y => y.Password).WithMessage("Поля не співпадають!");
        }

        private bool IsUniqueEmail(string data) 
        {
            var user = _userManager.FindByEmailAsync(data).Result;
            return user == null;
        }
    }

    public class LoginValidator : AbstractValidator<LoginUserModel> 
    {
        private UserManager<AppUser> _userManager { get; set; }
        public LoginValidator(UserManager<AppUser> userManager)
        {
            _userManager = userManager;

            RuleFor(x => x.Email)
                .NotEmpty().WithMessage("Поле не може бути пустим!")
                .Must(isAvailable).WithMessage("Не існує облікового запису!");

            RuleFor(x => x.Password)
                .NotEmpty().WithMessage("Поле не може бути пустим!");
        }

        private bool isAvailable(string email) 
        {
            var user = _userManager.FindByEmailAsync(email).Result;
            return user != null;
        }
    }
}
