using Microsoft.AspNetCore.Identity;

namespace ImageSaveAndroidWithImageWorker.Data.Identity.Entities
{
    public class AppUserRole : IdentityUserRole<long>
    {
        public virtual AppUser User { get; set; }
        public virtual AppRole Role { get; set; }
    }
}
