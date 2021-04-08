package com.ep.restapi.controller.v1;

import com.ep.restapi.advice.exception.CUserNotFoundException;
import com.ep.restapi.domain.User;
import com.ep.restapi.model.response.CommonResult;
import com.ep.restapi.model.response.ListResult;
import com.ep.restapi.model.response.SingleResult;
import com.ep.restapi.repository.UserRepository;
import com.ep.restapi.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@Api(tags={"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {
    private final UserRepository userRepository;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공 후 access_token",required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다.")
    @GetMapping(value = "/users")
    public ListResult<User> findAllUser(){
        return  responseService.getListResult(userRepository.findAll());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공 후 access_token",required = false, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회",notes = "userId로 회원을 조회한다.")
    @GetMapping(value = "/user")
    public SingleResult<User> findUserById(
            @ApiParam(value = "언어",defaultValue = "ko") @RequestParam String lang
    ) throws Exception {
        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        log.info("회원 단건 조회 success");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        // 결과데이터가 단일건인 경우 getBasicResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(userRepository.findByUid(id).orElseThrow(CUserNotFoundException::new));
    }

    @ApiOperation(value = "회원 입력",notes = "회원을 입력한다.")
    @PostMapping(value = "/user")
    public SingleResult<User> save(@ApiParam(value = "회원ID",required = true) @RequestParam String uid,
                                           @ApiParam(value = "회원이름",required = true) @RequestParam String name){
        User user = User.builder()
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userRepository.save(user));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공 후 access_token",required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다.")
    @PutMapping(value = "/user")
    public SingleResult<User> save(@ApiParam(value = "회원번호",required = true) @RequestParam long msrl,
                     @ApiParam(value = "회원이름",required = true) @RequestParam String name){
        User user = User.builder()
                .msrl(msrl)
                .name(name)
                .build();
        return responseService.getSingleResult(userRepository.save(user));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공 후 access_token",required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제",notes = "userId로 회원정보를 삭제한다.")
    @DeleteMapping(value = "/user/{msrl}")
    public CommonResult delete(
            @ApiParam(value = "회원번호",required = true) @PathVariable long msrl
    ){
      userRepository.deleteById(msrl);
      // 성공 결과 정보만 필요한 경우 getSuccessResult() 이용
        return responseService.getSuccessResult();
    }
}
