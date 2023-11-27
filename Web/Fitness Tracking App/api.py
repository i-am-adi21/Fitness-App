from flask import Blueprint, request
# import demjson
from db import *
import os

def encode(data):
	return str(data)

api = Blueprint('api',__name__)

@api.route('/login/', methods=['get','post'])
def login():
	data = {}
	username = request.args['username']
	password = request.args['password']
	q = "SELECT * FROM `login` WHERE `username` = '%s' AND `password` = '%s' AND `type` = 'user'" %(username, password)
	res = select(q)
	if (res):
		data['data'] = res
		data['status'] = 'success'
	else:
		data['status'] = 'failed'
	return encode(data)

@api.route('/register/', methods=['get','post'])
def register():
	data = {}
	first_name = request.args['first_name']
	last_name = request.args['last_name']
	phone = request.args['phone']
	email = request.args['email']
	place = request.args['place']
	username = request.args['username']
	password = request.args['password']
	gender = request.args['gender']
	dob = request.args['dob']

	log_qry = "INSERT INTO `login` (`username`, `password`, `type`) VALUES ('%s', '%s', 'user')" %(username, password)
	log_id = insert(log_qry)

	reg_qry = "INSERT INTO `users` (`logid`, `fname`, `lname`, `place`, `phone`, `email`) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')" %(log_id, first_name, last_name, place, phone, email)
	user_id = insert(reg_qry)

	q="insert into walkingdetails values (null ,'%s','0',curdate())"%(user_id)
	insert(q)

	health_qry = "INSERT INTO `healthprofile` (`userid`, `gender`, `Dob`, `blood_pressure`, `sugar_level`, `cholesterol_level`, `body_weight`, `Height`) VALUES ('%s', '%s', '%s', '', '', '', '', '')" %(user_id, gender, dob)
	insert(health_qry)



	data['status'] = 'success'
	return encode(data)

@api.route('/get_my_health_profile/', methods=['get','post'])
def get_my_health_profile():
	data = {}
	login_id = request.args['login_id']
	q = "SELECT * FROM `healthprofile` WHERE `userid` = (SELECT `userid` FROM `users` WHERE `logid` = '%s')" %(login_id)
	print(q)
	res = select(q)
	if (res):
		data['status'] = 'success'
		data['data'] = res
	else:
		data['status'] = 'failed'
	data['method'] = 'get_my_health_profile'
	return encode(data)

@api.route('/add_health_profile/', methods=['get','post'])
def add_health_profile():
	data = {}
	login_id = request.args['login_id']
	blood_pressure = request.args['blood_pressure']
	sugar_level = request.args['sugar_level']
	cholesterol_level = request.args['cholesterol_level']
	body_weight = request.args['body_weight']
	Height = request.args['Height']
	q = "UPDATE `healthprofile` SET `blood_pressure` = '%s', `sugar_level` = '%s', `cholesterol_level` = '%s', `body_weight`  = '%s', `Height` = '%s' WHERE `userid` =  '%s'" %(blood_pressure, sugar_level, cholesterol_level, body_weight, Height, login_id)
	print(q)
	update(q)

	
	data['status'] = 'success'

	data['method'] = 'add_health_profile'
	return encode(data)

@api.route('/food_intake/', methods=['get','post'])
def food_intake():
	data = {}
	login_id = request.args['login_id']
	time = request.args['food']
	qty = request.args['qty']
	fid=request.args['fid']

	q = "INSERT INTO `food` VALUES (null,'%s',(SELECT `userid` FROM `users` WHERE `logid` = '%s'), '%s', '%s','confirm',curdate())" %(fid,login_id,qty, time)
	insert(q)
	data['status'] = 'success'
	
	data['method'] = 'food_intake'
	return encode(data)

@api.route('/view_discussions/', methods=['get','post'])
def view_discussions():
	data = {}
	q = "SELECT * FROM `discussionmaster` ORDER BY `master_id` DESC"
	res = select(q)
	if (res):
		data['status'] = 'success'
		data['data'] = res
	else:
		data['status'] = 'failed'
	data['method'] = 'view_discussions'
	return encode(data)

@api.route('/view_discussion_details/', methods=['get','post'])
def view_discussion_details():
	data = {}
	discussion_id = request.args['discussion_id']
	q = "SELECT * FROM `discussiondetails` INNER JOIN `users` USING (`userid`) where master_id = '%s' ORDER BY `detail_id` DESC" %(discussion_id)
	res = select(q)
	if (res):
		data['status'] = 'success'
		data['data'] = res
	else:
		data['status'] = 'failed'
	data['method'] = 'view_discussion_details'
	return encode(data)

@api.route('/send_discussion_opinion/', methods=['get','post'])
def send_discussion_opinion():
	data = {}
	login_id = request.args['login_id']
	discussion_id = request.args['discussion_id']
	opinion = request.args['opinion']
	q = "INSERT INTO `discussiondetails` (`master_id`, `userid`, `comment_description`, `details_date`) VALUES ('%s', (SELECT `userid` FROM `users` WHERE `logid` = '%s'), '%s', CURDATE())" %(discussion_id, login_id, opinion)
	res = insert(q)

	if (res):
		data['status'] = 'success'
	else:
		data['status'] = 'failed'
	data['method'] = 'send_discussion_opinion'
	return encode(data)

@api.route('/send_feedback/', methods=['get','post'])
def send_feedback():
	data = {}
	login_id = request.args['login_id']
	feedback = request.args['feedback']
	q = "INSERT INTO `feedback` (`userid`, `Description`, `reply`, `feedback_date`) VALUES ((SELECT `userid` FROM `users` WHERE `logid` = '%s'), '%s', 'pending', CURDATE())" %(login_id, feedback)
	res = insert(q)

	if (res):
		data['status'] = 'success'
	else:
		data['status'] = 'failed'
	data['method'] = 'send_feedback'
	return encode(data)

@api.route('/view_my_feedback/', methods=['get','post'])
def view_my_feedback():
	data = {}
	login_id = request.args['login_id']
	q = "SELECT * FROM `feedback` WHERE `userid` = (SELECT `userid` FROM `users` WHERE `logid` = '%s') ORDER BY feed_id DESC" %(login_id)
	res = select(q)
	if (res):
		data['status'] = 'success'
		data['data'] = res
	else:
		data['status'] = 'failed'
	data['method'] = 'view_my_feedback'
	return encode(data)

@api.route('/view_suggestions/', methods=['get','post'])
def view_suggestions():
	data = {}
	login_id = request.args['login_id']
	q = "SELECT * FROM `dietsuggested`  inner JOIN dietsrequest USING (dietsrequest_id) WHERE `userid` = (SELECT `userid` FROM `users` WHERE `logid` = '%s') ORDER BY `suggested_id` DESC" %(login_id)
	res = select(q)
	if (res):
		data['status'] = 'success'
		data['data'] = res
	else:
		data['status'] = 'failed'
	data['method'] = 'view_suggestions'
	return encode(data)

@api.route('/Viewvideos/', methods=['get','post'])
def Viewvideos():
	data = {}

	login_id = request.args['log_id']
	q = "SELECT * FROM  excersice   inner JOIN   excersicesrequest USING (excersicesrequest_id)WHERE `userid` = (SELECT `userid` FROM `users` WHERE `logid` = '%s') ORDER BY `video_id` DESC"%(login_id)
	res = select(q)

	print(q)
	if (res):
		data['status'] = 'success'
		data['data'] = res
	else:
		data['status'] = 'failed'
	data['method'] = 'public_view_videos'
	return encode(data)


@api.route('/viewdietrules', methods=['get','post'])
def viewdietrules():
	data = {}
	q = "SELECT * FROM `generaldiet` "
	res = select(q)
	if (res):
		data['status'] = 'success'
		data['data'] = res
	else:
		data['status'] = 'failed'
	data['method'] = 'viewdietrules'
	return encode(data)


@api.route('/makepayment', methods=['get','post'])
def makepayment():
	data = {}
	login_id=request.args['lid']
	q="insert into payment values(null,'%s','1000',curdate(),'paid')"%(login_id)
	insert(q)
	data['status'] = 'success'
	data['method'] = 'viewdietrules'
	return encode(data)


@api.route('/Excersice_Request', methods=['get','post'])
def Excersice_Request():
	data = {}
	requestssss=request.args['request']
	login_id=request.args['login_id']
	q="insert into excersicesrequest values(null,(select userid  from users where logid='%s'),'%s',curdate(),'pending')"%(login_id,requestssss)
	insert(q)
	data['status'] = 'success'
		

	return encode(data)


@api.route('/Diet_Request', methods=['get','post'])
def Diet_Request():
	data = {}
	requestssss=request.args['request']
	login_id=request.args['login_id']
	q="insert into dietsrequest values(null,(select userid  from users where logid='%s'),'%s',curdate(),'pending')"%(login_id,requestssss)
	insert(q)
	
	data['status'] = 'success'
		
	
	data['method'] = 'viewdietrules'
	return encode(data)


@api.route('/viewhealthagent', methods=['get','post'])
def viewhealthagent():
	data = {}
	q = "SELECT * FROM `healthagent` "
	res = select(q)
	if (res):
		data['status'] = 'success'
		data['data'] = res
	else:
		data['status'] = 'failed'
	data['method'] = 'viewhealthagent'
	return encode(data)



@api.route("/chatdetail")
def chatdetail():
	sid=request.args['sender_id']
	rid=request.args['hid']
	
	data={}
	q="select * from chat where (sender_id='%s' and receiver_id='%s') or (sender_id='%s' and receiver_id='%s') group by chat_id "%(sid,rid,rid,sid)
	
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		
		data['status']="failed"
	data['method']='chatdetail'
	
	return str(data)

@api.route("/chat")
def chat():
    data={}
    sid=request.args['sender_id']
    rid=request.args['hid']
    det=request.args['details']

    q="insert into chat values(null,'%s','%s','%s',curdate())"%(sid,rid,det) 
    insert(q)
    data['status']='success'
    data['method']='chat'
    return str(data)


@api.route('/HealthProfile')
def HealthProfile():
	data = {}
	q = "SELECT * FROM `healthprofile` "
	res = select(q)
	if (res):
		data['status'] = 'success'
		data['data'] = res
	else:
		data['status'] = 'failed'
	data['method'] = 'get_my_health_profile'
	return encode(data)



@api.route('/stepcount')
def stepcount():
	data={}
	count=request.args['count']
	log_id=request.args['log_id']

	

	q="select * from walkingdetails where userid=(select userid from users where logid='%s') and walkingdate = curdate()"%(log_id)
	res=select(q)
	if res:
		
		q="update walkingdetails set steps_count=steps_count+'%s'  where userid=(select userid from users where logid='%s')"%(count,log_id)
		update(q)

		data['status']="success"
		data['method']="stepcount"
	return str(data)


@api.route('/viewpayment')
def viewpayment():
	data={}
	
	log_id=request.args['log_id']
	q="select * from payment where user_id='%s'"%(log_id)
	res=select(q)
	data['data']=res
	data['status']="success"
	data['method']="viewpayment"
	return str(data)


@api.route('/Viewfood')
def Viewfood():
	data={}
	
	
	q="select * from foodintakes "
	res=select(q)
	data['data']=res
	data['status']="success"

	return str(data)


@api.route('/searchfood')
def searchfood():
	data={}
	search=request.args['search']+'%'
	
	
	q="select * from foodintakes  where fooddetails like '%s'  or Calories like '%s' "%(search,search)
	res=select(q)
	data['data']=res
	data['status']="success"

	return str(data)


@api.route('/water')
def water():
	data={}
	water=request.args['water']
	login_id=request.args['login_id']

	q="insert into water values(null,(select userid from users where logid='%s'),'%s',curdate())"%(login_id,water)
	insert(q)
	data['status']="success"
	return str(data)




@api.route('/Uploadvideo',methods=['get','post'])
def Uploadvideo():
	data={}
	video=request.form['video']
	path="static/img"+str(uuid.uuid4())+video.filename
	video.save(path)
	login_id=request.form['lid']

	q="insert into userupload values(null,(select userid from users where logid='%s'),'%s',curdate())"%(login_id,path)
	insert(q)
	data['status']="success"
	return str(data)




@api.route('/update_health_profile/',methods=['get','post'])
def update_health_profile():
	data={}
	b=request.args['blood_pressure']
	s=request.args['sugar_level']
	c=request.args['cholesterol_level']
	w=request.args['body_weight']
	h=request.args['Height']
	pid=request.args['pid']


	q="update healthprofile set `blood_pressure`='%s',`sugar_level`='%s',`cholesterol_level`='%s',`body_weight`='%s',`Height`='%s'  where profile_id='%s'"%(b,s,c,w,h,pid)
	update(q)
	data['status']="success"
	return str(data)