using Microsoft.AspNetCore.Identity;

namespace ImageSaveAndroidWithImageWorker.Data.Identity.Entities
{
    public class AppRole : IdentityRole<long>
    {
        public virtual ICollection<AppUserRole> UserRoles { get; set; }
    }
}
