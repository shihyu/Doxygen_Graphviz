#pragma once

#include "RequestModel.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace chainofresponsibility
			{
				namespace example5
				{
					///
					/// <summary> * ��װ��Ԥ֧���÷�����ҵ����ص��������� </summary>
					/// 
					class PreFeeRequestModel : public RequestModel
					{
					///	
					///	 <summary> * Լ�������ҵ������ </summary>
					///	 
					public:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static const std::string FEE_TYPE = "preFee";
						PreFeeRequestModel();
					///	
					///	 <summary> * ������ </summary>
					///	 
					private:
						std::string user;
					///	
					///	 <summary> * ������ </summary>
					///	 
						double fee;
					public:
						virtual std::string getUser();
						virtual void setUser(std::string user);
						virtual double getFee();
						virtual void setFee(double fee);
					};

				}
			}
		}
	}
}