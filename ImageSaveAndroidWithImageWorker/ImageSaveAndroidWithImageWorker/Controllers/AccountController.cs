using AutoMapper;
using ImageSaveAndroidWithImageWorker.Data;
using ImageSaveAndroidWithImageWorker.Data.Identity.Entities;
using ImageSaveAndroidWithImageWorker.Models;
using ImageSaveAndroidWithImageWorker.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using System.Drawing;

namespace ImageSaveAndroidWithImageWorker.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AccountController : ControllerBase
    {
        private UserManager<AppUser> _userManager { get; set; }
        private IJwtTokenService _jwtTokenService { get; set; }
        private IMapper _mapper { get; set; }
        private EFContext _context { get; set; }

        public AccountController(UserManager<AppUser> userManager, IJwtTokenService jwtTokenService,
            IMapper mapper, EFContext context)
        {
            _userManager = userManager;
            _jwtTokenService = jwtTokenService;
            _mapper = mapper;
            _context = context;
        }

        [HttpPost]
        [Route("register")]
        public async Task<IActionResult> RegisterUser([FromBody] RegisterUserModel register) 
        {

            return await Task.Run(async () => { 
                AppUser user = _mapper.Map<AppUser>(register);
                IActionResult actionResult = BadRequest(new
                {
                    register = "Помилка створення користувача"
                });
                string randomFileName = Path.GetRandomFileName() + ".jpeg";
                user.Photo = !string.IsNullOrEmpty(register.Photo) ? randomFileName: "";
                string dirPath = Path.Combine(Directory.GetCurrentDirectory(), "Images", randomFileName);

                var result = await _userManager.CreateAsync(user, register.Password);
                if (!result.Succeeded)
                    return actionResult;
                else 
                {
                    if (!string.IsNullOrEmpty(register.Photo)) 
                    {
                        Bitmap bmp = ImageWorker.ConvertToImage(register.Photo);
                        bmp.Save(dirPath);
                    }
                }

                actionResult = BadRequest(new
                {
                    register = "Помилки надання ролі"
                });
                result = await _userManager.AddToRoleAsync(user, Constants.Constants.USER);
                if (!result.Succeeded)
                    return actionResult;

                actionResult = Ok(new { 
                    token = _jwtTokenService.CreateToken(user)
                });

                return actionResult;
            });

        }

        [HttpPost]
        [Route("login")]
        public async Task<IActionResult> LoginUser([FromBody] LoginUserModel login) 
        {
            return await Task.Run(() => {

                var user = _userManager.FindByEmailAsync(login.Email).Result;

                bool isCorrect = _userManager.CheckPasswordAsync(user, login.Password).Result;

                IActionResult res = BadRequest(new { 
                    errors =new { 
                        password = "Пароль введено не коректно!"
                    }
                });

                if (isCorrect) 
                {
                    res = Ok(new { 
                        token = _jwtTokenService.CreateToken(user)
                    });
                }

                return res;
            });
        }

        [HttpGet]
        [Route("users")]
        public async Task<IActionResult> GetAllUsers() 
        {
            Thread.Sleep(3000);
            return await Task.Run(() => {
                return Ok(_context.Users.Select(x => 
                _mapper.Map<UserItemViewModel>(x)).ToList());
            });
        }
    }


}
