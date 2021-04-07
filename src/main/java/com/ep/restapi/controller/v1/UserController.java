package com.ep.restapi.controller.v1;

import com.ep.restapi.domain.User;
import com.ep.restapi.model.response.CommonResult;
import com.ep.restapi.model.response.ListResult;
import com.ep.restapi.model.response.SingleResult;
import com.ep.restapi.repository.UserRepository;
import com.ep.restapi.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags={"1. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {
    private final UserRepository userRepository;
    private final ResponseService responseService;

    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다.")
    @GetMapping(value = "/user")
    public ListResult<User> findAllUser(){
        return  responseService.getListResult(userRepository.findAll());
    }

    @ApiOperation(value = "회원 단건 조회",notes = "userId로 회원을 조회한다.")
    @GetMapping(value = "/user/{msrl}")
    public SingleResult<User> findUserById(@ApiParam(value = "회원ID",required = true) @PathVariable  long msrl){
        // 결과데이터가 단일건인 경우 getBasicResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(userRepository.findById(msrl).orElse(null));
    }

    @ApiOperation(value = "회원 입력",notes = "회원을 입력한다.")
    @PostMapping(value = "/user")
    public SingleResult<User> save(@ApiParam(value = "회원ID",required = true) String uid,
                                           @ApiParam(value = "회원이름",required = true) String name){
        User user = User.builder()
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userRepository.save(user));
    }

    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다.")
    @PutMapping(value = "/user")
    public SingleResult<User> save(@ApiParam(value = "회원번호",required = true) long msrl,
                     @ApiParam(value = "회원아이디",required = true) String uid,
                     @ApiParam(value = "회원이름",required = true) String name){
        User user = User.builder()
                .msrl(msrl)
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userRepository.save(user));
    }

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
