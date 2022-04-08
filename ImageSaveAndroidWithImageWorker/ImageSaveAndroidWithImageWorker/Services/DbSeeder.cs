using ImageSaveAndroidWithImageWorker.Data.Identity.Entities;
using Microsoft.AspNetCore.Identity;

namespace ImageSaveAndroidWithImageWorker.Services
{
    public static class DbSeeder
    {
        public static void SeedIdentity(this IApplicationBuilder app) 
        {
            using (var scope = app.ApplicationServices.GetRequiredService<IServiceScopeFactory>().CreateScope()) 
            {
                var roleManager = scope.ServiceProvider.GetRequiredService<RoleManager<AppRole>>();

                if (!roleManager.Roles.Any()) 
                {
                    var res = roleManager.CreateAsync(new AppRole { 
                        Name = Constants.Constants.USER
                    }).Result;
                    res = roleManager.CreateAsync(new AppRole { 
                        Name = Constants.Constants.ADMIN
                    }).Result;
                }
            }
        }
    }
}
