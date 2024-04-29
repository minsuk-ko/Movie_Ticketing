/**
 * 회원가입 조건 >
 * 받아온 이메일이 DB에 이미 존재하는 이메일이면 안된다.
 * 이메일 형식에 맞지 않으면 안된다.
 * 입력한 비밀번호와 비밀번호 확인란에 기재한 문자는 같아야한다 .
 * */


// 인증번호 발송
function sendEmail() {
    const email = $("#email").val(); // 사용자가 입력한 이메일 주소
    // 이메일칸이 비어있으면 알림
    if (!email || email.trim() === '') {
        alert("이메일 주소를 입력해주세요.");
        return;
    }

    $.ajax({
        url: "/infoCheck/sendEmail" ,// @PostMapping("/infoCheck/sendEmail") EmailController
        type: "POST",
        data: {
            "email": email
        },
        success: function(data) {
            // 이메일 전송 성공
            alert("인증번호가 전송되었습니다.");
        },
        error: function() {
            // 이메일 전송 실패
            alert("인증번호 전송에 실패했습니다.");
        }
    });
}

// 인증번호 확인
function verifyCode() {
    const inputCode = $("#inputCode").val(); // 사용자가 입력한 인증번호
    if (!inputCode || inputCode.trim() === '') { // 인증번호입력없이 확인하기를 눌렀을 경우 알림
        alert("인증번호를 입력해주세요.");
        return;
    }

    $.ajax({
        url: "/infoCheck/verifyCode",
        type: "POST",
        data: {
            "inputCode": inputCode
        },
        success: function(isCodeCorrect) {
            if (isCodeCorrect) {
                alert("인증번호가 일치합니다.");
            } else {
                alert("인증번호가 일치하지 않습니다.");
                $("#inputCode").focus();
            }
        },
        error: function() {
            alert("인증번호 확인에 실패했습니다.");
        }
    });
}

//비밀번호 확인(span.innerHtml)
function  chackPassword(){
    var password = $("#password");
    var confirmPassword = $("#confirmPassword");
    var confrimMsg = $("#confirmMsg");
    var correctColor = "green";	//맞았을 때 출력되는 색깔.
    var wrongColor ="red";	//틀렸을 때 출력되는 색깔
    if(password.value == confirmPassword.value){
        confirmMsg.style.color = correctColor;/* span 태그의 ID(confirmMsg) 사용  */
        confirmMsg.innerHTML ="비밀번호 일치";
    } else{
        confirmMsg.style.color = wrongColor;
        confirmMsg.innerHTML ="비밀번호 불일치";
    }
}

$(document).ready(function() {
    $('#password').on('keyup', function() {
        var password = $(this).val();
        var strength = '';
        if (password.length < 7) {
            strength = 'week';
            $('#password-strength').css('color', 'red');
        } else if (password.length >= 8) {
            strength = 'good!';
            $('#password-strength').css('color', 'green');
        }
        $('#password-strength').text(strength);
    });
});


function SignUpFormSumitCheck(event) {
    // 폼 제출을 막기 위해 event.preventDefault() 호출
    event.preventDefault();

    // 입력된 이메일, 비밀번호, 비밀번호 확인 값을 가져옴
    const email = $("#email").val(); // 이메일
    const password = $("#password").val(); // 비밀번호
    const confirmPassword = $("#confirmPassword").val(); // 비밀번호 확인
    const inputCode = $("#inputCode").val(); // 인증번호 확인

    if (!isEmailValid(email)) {
        alert("올바른 이메일 형식이 아닙니다.");
        $("#email").focus();
        return;
    }


    // 인증번호 확인 AJAX 요청
    $.ajax({
        url: "/infoCheck/verifyCode",
        type: "POST",
        data: {
            "inputCode": inputCode
        },
        success: function(isCodeCorrect) {
            if (!isCodeCorrect) {
                alert("인증번호가 일치하지 않습니다.");
                return;
            }

            // 인증번호가 일치하면 회원가입 양식 확인 AJAX 요청
            const ajaxPromise = new Promise((resolve, reject) => {
                $.ajax({
                    type: "post",
                    url: "/SignUpForm/SignUpFormSumitCheck",
                    data: {
                        "email": email,
                        "password": password,
                        "confirmPassword": confirmPassword,
                        "inputCode": inputCode,
                    },
                    success: function(res) {
                        resolve(res);
                    },
                    error: function(err) {
                        reject(err);
                    }
                });
            });

            ajaxPromise.then((res) => {
                if (res == "Emailno") {
                    alert("이메일을 확인해주세요.");
                    document.getElementById("email").focus();

                } else if (res == "PWno") {
                    alert("비밀번호를 확인해주세요.");
                    document.getElementById("confirmPassword").focus();

                } else {
                    alert("회원가입 성공.");
                    console.log("res : ", res);
                    document.getElementById("SignUpForm").submit();
                }
            }).catch((err) => {
                console.log("에러발생: ", err);
            });

        },
        error: function() {
            alert("인증번호 확인에 실패했습니다.");
        }
    });
}


// 이메일 형식 확인하는 함수
function isEmailValid(email) {
    const emailRegex = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/;
    return emailRegex.test(email);
}


function emailDuplicateCheck(){
    const email = $("#email").val();
    // <span id="checkResult">      </span>에 출력하기
    const checkResult = $("#checkResult");
    console.log("입력값 :" , email);

    // 형식
    if (!isEmailValid(email)) {
        console.log("올바른 이메일 형식이 아닙니다.");
        checkEmail.style.color = "red";
        checkEmail.innerHTML = "올바른 이메일 형식이 아닙니다.";
        return;
    }

    $.ajax({
        type:"post",
        url:"/SignUpForm/emailDuplicateCheck",
        data: {
            "email": email // "전송되는 파라미터 값 이름 ": 파라미터
        },
        success : function(res){
            console.log("요청성공 : ", res);
            if(res == "ok"){
                console.log("사용가능한 이메일입니다.");
                checkEmail.style.color = "green";
                checkEmail.innerHTML="사용가능한 이메일입니다.";
            } else {
                console.log("이미 사용중인 이메일입니다.");
                checkEmail.style.color = "red";
                checkEmail.innerHTML="이미 사용중인 이메일입니다.";
            }
        },
        error : function(err){
            console.log("에러발생: ",err);
        }

    })
}