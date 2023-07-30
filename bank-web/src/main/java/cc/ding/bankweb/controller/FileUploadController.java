package cc.ding.bankweb.controller;

import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.Result;
import cc.ding.bankweb.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

@RestController
@CrossOrigin
public class FileUploadController {

    @Autowired
    private TokenUtils tokenUtils;
    @Value("${file.upload-path}")
    private String uploadPath;

    @RequestMapping("/api/upload")
    public Result upload(@RequestHeader("Token") String token, @RequestParam("file") MultipartFile file) {
        Account account = tokenUtils.getAccount(token);
        String username = account.getUsername();
        try {
            String fileType = Objects.requireNonNull(file.getContentType()).split("/")[1];
            //拿到图片上传到的目录(类路径classes下的static/img)的File对象
            File uploadDirFile = ResourceUtils.getFile(uploadPath);
            //拿到图片上传到的目录的磁盘路径
            String uploadDirPath = uploadDirFile.getAbsolutePath();
            //拿到图片保存到的磁盘路径
            String fileUploadPath = uploadDirPath + "/" + username + "." + fileType;
            //保存图片
            file.transferTo(new File(fileUploadPath));
            //成功响应
            return Result.ok("头像上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.err(Result.CODE_ERR_BUSINESS, "头像上传失败！");
        }
    }
}
