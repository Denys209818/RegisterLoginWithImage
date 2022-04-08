using Microsoft.AspNetCore.Identity;

namespace ImageSaveAndroidWithImageWorker.Data.Identity.Entities
{
    public class AppUser : IdentityUser<long>
    {
        public string FirstName { get; set; }
        public string SecondName { get; set; }
        public string Phone { get; set; }
        public string Photo { get; set; }
        public virtual ICollection<AppUserRole> UserRoles { get; set; }
    }
}
