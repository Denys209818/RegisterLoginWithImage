using ImageSaveAndroidWithImageWorker.Data.Identity.Entities;
using Microsoft.AspNetCore.Identity;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace ImageSaveAndroidWithImageWorker.Services
{
    public interface IJwtTokenService 
    {
        string CreateToken(AppUser user);
    }
    public class JwtTokenService : IJwtTokenService
    {
        private readonly UserManager<AppUser> _userManager;
        private readonly IConfiguration _configuration;
        public JwtTokenService(UserManager<AppUser> userManager, IConfiguration configuration)
        {
            _userManager = userManager;
            _configuration = configuration;

        }
        public string CreateToken(AppUser user)
        {
            List<Claim> claims = new List<Claim>();

            claims.Add(new Claim("email", user.Email));
            claims.Add(new Claim("firstname", user.FirstName));
            claims.Add(new Claim("secondname", user.SecondName));
            claims.Add(new Claim("phone", user.Phone));
            claims.Add(new Claim("photo", user.Photo));

            foreach (var role in _userManager.GetRolesAsync(user).Result)
            {
                claims.Add(new Claim("roles", role));
            }

            var symmetricSecurityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(
                _configuration.GetValue<String>("private_key")));

            var signInCredentials = new SigningCredentials(symmetricSecurityKey, SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(claims: claims, signingCredentials: signInCredentials,
                expires: DateTime.Now.AddDays(100));

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}
