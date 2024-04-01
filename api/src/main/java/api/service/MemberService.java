package api.service;

import api.common.error.GeneralErrorCode;
import api.common.error.MemberErrorCode;
import api.common.error.TokenErrorCode;
import api.common.exception.ResultException;
import api.config.jwt.JwtProvider;
import api.config.jwt.TokenDto;
import api.converter.MemberConverter;
import api.converter.RefreshTokenConverter;
import api.model.request.MemberLoginRequest;
import api.model.request.MemberRegisterRequest;
import api.model.response.MemberLoginResponse;
import api.model.response.MemberResponse;
import db.entity.MemberEntity;
import db.entity.RefreshTokenEntity;
import db.enums.MemberStatus;
import db.repository.MemberRepository;
import db.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenConverter refreshTokenConverter;

    public void saveProfileImageLocally(byte[] profileImage,
                                        String filename,
                                        String format) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(profileImage);
        BufferedImage image = ImageIO.read(inputStream);
        File output = new File(filename);
        ImageIO.write(image, format, output);
    }

    public MemberResponse register(MemberRegisterRequest request){
        // 중복 이메일 확인
        if(memberRepository.findFirstByUsernameAndStatus(
                request.getUsername(),
                MemberStatus.REGISTERED).isPresent()){
            throw new ResultException(MemberErrorCode.DUPLICATE_USERNAME);
        }

        // TODO: 이미지 저장 클래스 따로 분리하여 작성하기
        // 전달받은 프로필 이미지 존재 시
//        if (request.getProfileImage()!=null){
//            // 로컬 파일 주소
//            // classloader 사용?
//            String filename = "\\resources\\images\\"+request.getUsername().split("@")[0]+".png";
//            try{
//                byte[] blobData = Base64.getDecoder().decode(request.getProfileImage());
//                saveProfileImageLocally(blobData,
//                        filename,
//                        "png");
//                // 파일 경로를 DB에 저장, 파일 이름은 {username}.png
//                request.setProfileImage(filename);
//            } catch (IOException e){
//                throw new ResultException(MemberErrorCode.IMAGE_ERROR);
//            }
//        } else { // 프로필 사진이 없을 때 디폴트 사진 경로 저장
//            request.setProfileImage("\\resources\\images\\default.png");
//        }

        // 요청에서 받은 비밀번호 암호화하여 저장
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        MemberEntity entity = memberConverter.toEntity(request);
        MemberEntity newEntity = memberRepository.save(entity);
        return memberConverter.toResponse(newEntity);
    }

    public MemberLoginResponse signIn(MemberLoginRequest request) {
        // username이 일치하고 등록된 상태인 회원을 찾음
        MemberEntity member = memberRepository.findFirstByUsernameAndStatus(
                        request.getUsername(),
                        MemberStatus.REGISTERED
                // 요청으로 받은 password를 passwordEncoder로 암호화한 결과가 db에 저장된 암호화된 password와 일치하는지 확인
                ).filter(it -> passwordEncoder.matches(
                        request.getPassword(),
                        it.getPassword()))
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NULL_POINT));

        // token 발행 - refreshToken에는 만료시간만
        TokenDto refreshToken = jwtProvider.generateRefreshToken();

        // 남아있을지도 모르는 refresh token 삭제
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findFirstByMemberId(member.getId());
        refreshTokenEntity.ifPresent(refreshTokenRepository::delete);

        // 새 refreshToken DB에 저장
        RefreshTokenEntity newRefreshTokenEntity = refreshTokenConverter.toEntity(
                member,
                refreshToken);
        refreshTokenRepository.save(newRefreshTokenEntity);

        // token 발행 - accessToken에는 유저정보 포함
        Map<String, Object> data = new HashMap<>();
        data.put("username",member.getUsername());
        data.put("type",member.getType());
        TokenDto accessToken = jwtProvider.generateAccessToken(data);

        return memberConverter.toLoginResponse(
                member,
                accessToken.getToken(),
                refreshToken.getToken());
    }

    public MemberResponse info(){
        // 현재 로그인한 멤버 정보 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberEntity memberEntity = memberRepository.findFirstByUsernameAndStatus(
                authentication.getName(),
                MemberStatus.REGISTERED
        ).orElseThrow(() -> new ResultException(TokenErrorCode.TOKEN_EXCEPTION));

        return memberConverter.toResponse(memberEntity);
    }

    public void memberLogout(){
        // 현재 로그인한 멤버 정보 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        MemberEntity memberEntity = memberRepository.findFirstByUsernameAndStatus(
                authentication.getName(),
                MemberStatus.REGISTERED
        ).orElseThrow(() -> new ResultException(TokenErrorCode.TOKEN_EXCEPTION));

        // 멤버에게 발급된 refresh token db에서 삭제
        jwtProvider.invalidateRefreshToken(memberEntity.getId());

        // 현재 로그인한 멤버 정보 지움
        SecurityContextHolder.clearContext();
    }

}
