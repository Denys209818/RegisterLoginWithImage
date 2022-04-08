using FluentValidation.AspNetCore;
using ImageSaveAndroidWithImageWorker.Data;
using ImageSaveAndroidWithImageWorker.Data.Identity.Entities;
using ImageSaveAndroidWithImageWorker.Mappers;
using ImageSaveAndroidWithImageWorker.Services;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Cors.Infrastructure;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.FileProviders;
using Microsoft.IdentityModel.Tokens;
using System.Text;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers().AddNewtonsoftJson(options =>
{
    options.SerializerSettings.ContractResolver = new CamelCasePropertyNamesContractResolver();
    options.SerializerSettings.DefaultValueHandling = DefaultValueHandling.Include;
    options.SerializerSettings.NullValueHandling = NullValueHandling.Ignore;
});
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddDbContext<EFContext>((DbContextOptionsBuilder build) => {
    build.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection"));
});

builder.Services.AddIdentity<AppUser, AppRole>((IdentityOptions opts) => {
    opts.Password.RequireNonAlphanumeric = false;
    opts.Password.RequireDigit = false;
    opts.Password.RequireLowercase = false;
    opts.Password.RequireUppercase = false;
    opts.Password.RequiredLength = 5;
})
    .AddEntityFrameworkStores<EFContext>()
    .AddDefaultTokenProviders();

var signInKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(builder.Configuration.GetValue<String>("private_key")));

builder.Services.AddAuthentication((AuthenticationOptions opts) => {
    opts.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    opts.DefaultForbidScheme = JwtBearerDefaults.AuthenticationScheme;
}).AddJwtBearer((JwtBearerOptions jwtOpts) => { 
    jwtOpts.RequireHttpsMetadata = false;
    jwtOpts.SaveToken = true;
    jwtOpts.TokenValidationParameters = new TokenValidationParameters
    {
        IssuerSigningKey = signInKey,
        ValidateIssuer = true,
        ValidateLifetime = false,
        ValidateIssuerSigningKey = true,
        ValidateAudience = false,
    };
});

builder.Services.AddScoped<IJwtTokenService, JwtTokenService>();
builder.Services.AddAutoMapper(typeof(AccountMapper));
builder.Services.AddFluentValidation(opts => 
opts.RegisterValidatorsFromAssemblyContaining<Program>());

builder.Services.AddCors();


var app = builder.Build();

app.UseCors((CorsPolicyBuilder builder) => {
    builder.AllowAnyHeader().AllowAnyOrigin().AllowAnyMethod();
    });

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
    app.SeedIdentity();
}

app.UseAuthorization();

string dirPath = Path.Combine(Directory.GetCurrentDirectory(), "Images");
if(!Directory.Exists(dirPath))
    Directory.CreateDirectory(dirPath);

app.UseStaticFiles(new StaticFileOptions {
    FileProvider = new PhysicalFileProvider(dirPath),
    RequestPath = "/images"
});

app.MapControllers();

app.Run();
