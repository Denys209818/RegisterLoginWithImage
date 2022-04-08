using System.Drawing;

namespace ImageSaveAndroidWithImageWorker.Services
{
    public static class ImageWorker
    {
        public static Bitmap ConvertToImage(string base64) 
        {
            try
            {
                byte[] img = Convert.FromBase64String(base64);
                using (MemoryStream memory = new MemoryStream(img))
                {
                    memory.Position = 0;
                    Image image = Image.FromStream(memory);
                    memory.Close();
                    img = null;
                    return new Bitmap(image);
                }
            }
            catch 
            {
                return null;
            }
        }
    }
}
