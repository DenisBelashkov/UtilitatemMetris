from module import Module
from flask import request, jsonify
from datetime import date, datetime
from decoration import wrapper_for_token
from sqlalchemy import and_
import jwt
import logging

def address_to_string(a):
	str1 = ""
	while True:
		str1 += a.type_address.name + ": " + str(a.name) + ", "
		if not a.id_include:
			return str1
		a = a.address


class PaymentModule(Module):

	def setOptions(self, app, db, base):
		Flat = base.classes.Personal_account
		Payment = base.classes.Payment_history
		Metrics = base.classes.Metrics
		Tariff = base.classes.Tariff
		Type_metric = base.classes.Type_metrics
		Users = base.classes.Users
		Address = base.classes.Address

		def metric_to_dict2(metric):
			return {"identifier": metric.Metrics.identifier,
					"balance": float(metric.Metrics.balance),
					"prevValue": float(metric.Metrics.prev_value),
					"currValue": float(metric.Metrics.curr_value),
					"tariff": float(metric.price),
					"typeMetric": metric.name,
					"address": address_to_string(metric.Address)}

		def payment_to_string(payments):
			res_list = []
			for payment in payments:
				res_list.append({"prevValue": float( payment.Payment_history.prev_value),
								"currValue": float(payment.Payment_history.curr_value),
								"cost": float(payment.Payment_history.cost),
								"metric": metric_to_dict2(payment)
								 })
			return res_list

		def all_payment_to_string(payments, email, date, id_session):
			return {"email": email,
					"date":date.strftime("%Y-%m-%dT%H:%M:%S.0Z"),
					"id": id_session,
					"metrics": payment_to_string(payments)}

		@app.route('/payment/history', methods=["POST"])
		@wrapper_for_token
		def payment_history():
			try:
				r_json = request.json
				logging.warning(r_json)
				decode_token = jwt.decode(request.headers["token"], "secret", algorithms=["HS256"])
				#decode_token = {"id": 2, "email": "milo2", "type": "user"}
				if decode_token["type"] == "demo":
					return " ", 403
				id_user = decode_token["id"]
				payments = db.session.query(Payment, Metrics, Users).join(Metrics, Metrics.id_metrics == Payment.id_metrics).join(Users, Users.id_user == Payment.id_user)
				if r_json["identifierMetric"] is not None:
					payments = payments.filter(Metrics.identifier == r_json["identifierMetric"])
				if r_json["typeMetric"] is not None:
					payments = payments.join(Type_metric, and_(Type_metric.id_type == Metrics.id_type, Type_metric.name == r_json["typeMetric"]))
				if r_json["dateWith"] is not None:
					date_1 = datetime.strptime(r_json["dateWith"], '%d-%m-%Y %H:%M:%S').date()
					payments = payments.filter(Payment.date > date_1)
				if r_json["dateTo"] is not None:
					date_2 = datetime.strptime(r_json["dateTo"], '%d-%m-%Y %H:%M:%S').date()
					payments = payments.filter(Payment.date < date_2)
				payments = payments.all()
				res_list = []
				for payment in payments:
					all_payment = db.session.query(Payment, Metrics, Type_metric.name, Tariff.price, Address)\
						.join(Metrics, Metrics.id_metrics == Payment.id_metrics)\
						.join(Flat, Flat.id_personal_account == Metrics.id_personal_account)\
						.join(Address, Flat.id_address == Address.id_address)\
						.join(Tariff, Tariff.id_tariff == Metrics.id_tariff)\
						.join(Type_metric, Type_metric.id_type == Metrics.id_type)\
						.filter(Payment.id_session == payment.Payment_history.id_session).all()
					res_list.append(all_payment_to_string(all_payment, payment.Users.email, payment.Payment_history.date, payment.Payment_history.id_session))
				if len(payments) == 0:
					return jsonify([])
				return jsonify(res_list)
			except Exception as e:
				logging.warning(e)
				return "", 500

		# @app.route('/payment/metrics', methods=["POST"])
		# @wrapper_for_token
		# def payment_metrics():
		# 	try:
		# 		print(request.json)
		# 		res_list = []
		# 		json_request = request.json
		# 		for item in json_request:
		# 			metric = db.session.query(Metrics, Tariff.price)\
		# 				.join(Tariff,Tariff.id_tariff == Metrics.id_tariff) \
		# 				.filter(Metrics.id_metrics == item['idMetric']).one_or_none()
		# 			if not metric:
		# 				continue
		# 			cost = item["cost"] + metric.Metrics.balance
		# 			payment = Payment()
		# 			payment.id_metrics = metric.Metrics.id_metrics
		# 			payment.id_user = item["idUser"]
		# 			payment.prev_value = metric.Metrics.prev_value
		# 			payment.date = date.today()
		# 			payment.cost = item["cost"]
		# 			if cost <= 0:
		# 				metric.Metrics.balance = cost
		# 				payment.curr_value = metric.Metrics.prev_value
		# 			else:
		# 				need_cost = (metric.Metrics.curr_value - metric.Metrics.prev_value) * metric.price
		# 				metric.Metrics.balance = cost - need_cost
		# 				payment.curr_value = metric.Metrics.curr_value
		# 				metric.Metrics.prev_value = metric.Metrics.curr_value
		# 			db.session.add(payment)
		# 			db.session.commit()
		# 			res_list.append(
		# 				{"id": payment.id_payment_history,
		# 				 "date": payment.date,
		# 				 "cost": float(payment.cost),
		# 				 "prevValue": float(payment.prev_value),
		# 				 "currValue": float(payment.curr_value),
		# 				 "userName": " "})
		# 		return jsonify(res_list)
		# 	except Exception as e:
		# 		db.session.rollback()
		# 	return "", 400
		def make_payment(id_user, id_metric, cost, prev_value, curr_value)->Payment:
			payment = Payment()
			payment.id_metrics = id_metric
			payment.id_user = id_user
			payment.prev_value = prev_value
			payment.curr_value = curr_value
			payment.date = datetime.now()
			payment.cost = cost
			payment.id_session = 1
			return payment

		def metric_to_dict(metric):
			return {"identifier": metric.Metrics.identifier,
					"balance": float(metric.Metrics.balance),
					"prevValue": float(metric.Metrics.prev_value),
					"currValue": float(metric.Metrics.curr_value),
					"tariff": float(metric.price),
					"typeMetric": metric.name,
					"address": address_to_string(metric.Address)}

		def serializable_payment(payments, email):
			res_list = []
			date = None
			id_session = -1
			for payment in payments:
				if not date :
					date = payment[0].date
					id_session = payment[0].id_payment_history
				payment[0].id_session = id_session
				res_list.append({"cost": float(payment[0].cost),
							"prevValue": float(payment[0].prev_value),
							"currValue": float(payment[0].curr_value),
							"metric": metric_to_dict(payment[1])})
			db.session.commit()
			date = date.strftime("%Y-%m-%dT%H:%M:%S.0Z")
			return {"id": id_session, "date": date, "metrics": res_list, "email": email}

		@app.route('/payment/metrics', methods=["POST"])
		@wrapper_for_token
		def post_payment_metrics():
			try:
				decode_token = jwt.decode(request.headers["token"], "secret", algorithms=["HS256"])
				#decode_token = {"id": 3, "email" :"milo"}
				r_json = request.json
				cost = float(r_json["cost"])
				res_list = []
				for j_metric in r_json["metrics"]:
					metric = db.session.query(Metrics, Tariff.price, Address, Type_metric.name)\
						.join(Tariff, Tariff.id_tariff == Metrics.id_tariff) \
						.join(Type_metric, Type_metric.id_type == Metrics.id_type)\
						.filter(Metrics.identifier == j_metric["identifier"]).join(Flat, Flat.id_personal_account == Metrics.id_personal_account)\
						.join(Address, Address.id_address == Flat.id_address)\
						.first()
					if cost <= 0:
						break
					balance = float(metric.Metrics.balance)
					if balance > 0:
						continue
					payment: Payment = None
					if abs(balance) > cost:
						payment = make_payment(decode_token["id"],
														metric.Metrics.id_metrics,
														cost,
														metric.Metrics.prev_value,
														metric.Metrics.curr_value)
						cost = 0
					else:
						payment = make_payment(decode_token["id"],
									metric.Metrics.id_metrics,
									-balance,
									metric.Metrics.prev_value,
									metric.Metrics.curr_value)
						cost = cost + balance
					metric.Metrics.balance = balance + payment.cost
					db.session.add(payment)
					res_list.append((payment, metric))
				if len(res_list) == 0:
					return jsonify([])
				if cost - 0 > 10e-2:
					db.session.rollback()
					return " ", 406
				db.session.commit()
				return jsonify(serializable_payment(res_list, decode_token["email"]))
			except Exception as e:
				db.session.rollback()
				logging.warning(e)
				return " ", 500

		@app.route('/payment/update', methods=["POST"])
		def update_balance_of_metrics():
			try:
				metrics = db.session.query(Metrics, Tariff.price).join(Tariff, Tariff.id_tariff == Metrics.id_tariff).all()
				for metric in metrics:
					need_cost = float((metric.Metrics.curr_value - metric.Metrics.prev_value) * metric.price)
					metric.Metrics.balance = float(metric.Metrics.balance) - need_cost
					metric.Metrics.prev_value = metric.Metrics.curr_value
				db.session.commit()
				return " ", 200
			except Exception as e:
				logging.warning(e)
				db.session.rollback()
				return " ", 500

