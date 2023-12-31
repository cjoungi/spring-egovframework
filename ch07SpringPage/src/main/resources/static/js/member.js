$(function(){
	//MyPage 프로필 사진 등록 및 수정
	//수정 버튼 이벤트 처리
	$('#photo_btn').click(function(){
		$('#photo_choice').show();
		$(this).hide();
	});//end of click
	
	//처음 화면에 보여지는 이미지 읽기
	let photo_path = $('.my-photo').attr('src');
	let my_photo; //업로드하고자 선택한 이미지 저장
	
	$('#upload').change(function(){
		my_photo = this.files[0];
		if(!my_photo){
			$('.my-photo').attr('src',photo_path);
			return;
		}
	
	if(my_photo.size > 1024*1024){
		alert(Math.round(my_photo.size/1024) + 'kbytes(1024kbytes까지만 업로드 가능)');
		$('.my-photo').attr('src',photo_path);
		$(this).val('');
		return;
	}
	
	//이미지 미리보기 처리
	let reader = new FileReader();
	reader.readAsDataURL(my_photo);
	
	reader.onload=function(){
		$('.my-photo').attr('src',reader.result);
	};
	
	});//end of change
	
	//서버에 데이터 전송
	$('#photo_submit').click(function(){
		if($('#upload').val()==''){
			alert('파일을 선택하세요');
			$('#upload').focus();
			return;
		}
		//파일전송
		let form_data = new FormData();
		form_data.append('upload',my_photo);
		
		$.ajax({
			url:'../member/updateMyPhoto.do',
			type:'post',
			data:form_data,
			dataType:'json',
			contentType:false,
			enctype:'multipart/form-data',
			processData:false,
			success:function(param){
				if(param.result=='logout'){
					alert('로그인 후 사용하세요');
				}else if(param.result=='success'){
					alert('프로필 사진이 수정되었습니다.');
					//교체된 이미지 저장
					photo_path = $('.my-photo').attr('src');
					$('#upload').val('');
					$('#photo_choice').show();
					$('#photo_btn').hide();
				}else{
					alert('파일 전송 오류');
				}
			},
			error:function(){
				alert('네트워크 오류 발생');
			}
		});
	});//end of click(ajax)
	
	//취소 버튼 처리
	$('#photo_reset').click(function(){
		$('.my-photo').attr('src',photo_path);//초기화
		$('#upload').val('');
		$('#photo_choice').hide();
		$('#photo_btn').show();
	});	
	
	
	//비밀번호 변경 체크
	$('#passwd').keyup(function(){
		if($('#confirm_passwd').val()!='' && $('#confirm_passwd').val()!=$(this).val()){
			$('#message_id').text('비밀번호 불일치').css('color','red');
		}else if($('#confirm_passwd').val()!='' && $('#confirm_passwd').val()==$(this).val()){
			$('#message_id').text('비밀번호 일치').css('color','#000');
		}else if($('#confirm_passwd').val()=='' && $(this).val()==''){
			$('#message_id').text('');
		}
	});
	
	$('#confirm_passwd').keyup(function(){
		if($('#passwd').val()!='' && $('#passwd').val()!=$(this).val()){
			$('#message_id').text('비밀번호 불일치').css('color','red');
		}else if($('#passwd').val()!='' && $('#passwd').val()==$(this).val()){
			$('#message_id').text('비밀번호 일치').css('color','#000');
		}else if($('#passwd').val()=='' && $(this).val()==''){
			$('#message_id').text('');
		}
	});
	
	$('#change_form').submit(function(){
		if($('#now_passwd').val().trim()==''){
			alert('현재 비밀번호를 입력하세요');
			$('#now_passwd').val('').focus();
			return false;
		}
		if($('#passwd').val().trim()==''){
			alert('새 비밀번호를 입력하세요');
			$('#passwd').val('').focus();
			return false;
		}
		if($('#confirm_passwd').val().trim()==''){
			alert('비밀번호 확인을 입력하세요');
			$('#confirm_passwd').val('').focus();
			return false;
		}
		if($('#passwd').val()!=$('#confirm_passwd').val()){
			$('#message_id').text('비밀번호 불일치').css('color','red');
			return false;
		}
	})
});