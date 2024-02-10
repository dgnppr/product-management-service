package me.dgpr.config.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dgpr.domains.manager.domain.Manager;
import me.dgpr.domains.manager.usecase.QueryLogoutByIdUseCase;
import me.dgpr.domains.manager.usecase.QueryManagerByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwtTokenFilterTest {

    private static final String secretKey = "zOlJAgjm9iEZPqmzilEMh4NxvOfg1qBRP3xYkzUWpSE";
    @Mock
    private JwtTokenHandler jwtTokenHandler;
    @Mock
    private QueryManagerByIdUseCase queryManagerByIdUseCase;
    @Mock
    private QueryLogoutByIdUseCase queryLogoutByIdUseCase;

    private JwtTokenFilter sut;

    @BeforeEach
    void setUp() {
        sut = new JwtTokenFilter(
                secretKey,
                jwtTokenHandler,
                queryManagerByIdUseCase,
                queryLogoutByIdUseCase
        );
    }

    @Test
    void http_헤더에_토큰이_존재하지_않으면_authentication을_저장하지_않는다() throws Exception {
        //Arrange
        var req = mock(HttpServletRequest.class);
        var res = mock(HttpServletResponse.class);
        var chain = mock(FilterChain.class);

        //Act
        sut.doFilterInternal(
                req,
                res,
                chain
        );

        //Assert
        verify(jwtTokenHandler, never()).verifyAndGetIdFromToken(eq(secretKey), any());
        verify(chain, times(1)).doFilter(req, res);
    }

    @Test
    void 유효한_토큰이_아니면_authentication을_저장하지_않는다() throws Exception {
        //Arrange
        var req = mock(HttpServletRequest.class);
        var res = mock(HttpServletResponse.class);
        var chain = mock(FilterChain.class);

        var token = "token";

        when(req.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(token);

        when(jwtTokenHandler.verifyAndGetIdFromToken(secretKey, token))
                .thenThrow(JwtException.class);

        //Act
        sut.doFilterInternal(
                req,
                res,
                chain
        );

        //Assert
        verify(queryManagerByIdUseCase, never()).query(any());
        verify(chain, times(1)).doFilter(req, res);
    }

    @Test
    void 토큰에서_추출한_id로_manager를_조회하여_authentication을_저장한다() throws Exception {
        // Arrange
        String token = "token";
        Manager manager = mock(Manager.class);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(token);

        when(jwtTokenHandler.verifyAndGetIdFromToken(
                anyString(),
                anyString()
        ))
                .thenReturn(1L);

        when(queryManagerByIdUseCase.query(any(QueryManagerByIdUseCase.Query.class)))
                .thenReturn(manager);

        when(queryLogoutByIdUseCase.query(any(QueryLogoutByIdUseCase.Query.class)))
                .thenReturn(false);

        // Act
        sut.doFilterInternal(request, response, chain);

        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.getPrincipal()).isInstanceOf(ManagerContext.class);

        ManagerContext actual = (ManagerContext) authentication.getPrincipal();
        assertThat(actual).isNotNull();
        assertThat(actual.getManager()).isEqualTo(manager);

        SecurityContextHolder.clearContext();
    }

    @Test
    void 로그아웃된_토큰이면_authentication을_저장하지_않는다() throws Exception {
        //Arrange
        String token = "token";
        Manager manager = mock(Manager.class);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(token);

        when(jwtTokenHandler.verifyAndGetIdFromToken(
                anyString(),
                anyString()
        ))
                .thenReturn(1L);

        when(queryManagerByIdUseCase.query(any(QueryManagerByIdUseCase.Query.class)))
                .thenReturn(manager);

        when(queryLogoutByIdUseCase.query(any(QueryLogoutByIdUseCase.Query.class)))
                .thenReturn(true);

        //Act
        sut.doFilterInternal(request, response, chain);

        //Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }
}