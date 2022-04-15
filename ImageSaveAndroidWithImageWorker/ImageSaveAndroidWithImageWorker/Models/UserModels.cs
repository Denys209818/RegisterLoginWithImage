namespace ImageSaveAndroidWithImageWorker.Models
{
    public class RegisterUserModel
    {
        public string FirstName { get; set; }
        public string SecondName { get; set; }
        public string Phone { get; set; }
        public string Photo { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string ConfirmPassword { get; set; }
    }

    public class LoginUserModel 
    {
        public string Email { get; set; }
        public string Password { get; set; }
    }

    public class UserItemViewModel 
    {
        public string FirstName { get; set; }
        public string Image { get; set; }
    }
}
