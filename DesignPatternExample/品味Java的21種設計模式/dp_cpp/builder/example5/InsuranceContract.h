#pragma once

#include "ConcreteBuilder.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace builder
			{
				namespace example5
				{
					///
					/// <summary> * ���պ�ͬ�Ķ��� </summary>
					/// 
					class InsuranceContract
					{
					///	
					///	 <summary> * ���պ�ͬ��� </summary>
					///	 
					private:
						std::string contractId;
					///	
					///	 <summary> * ��������Ա�����ƣ�ͬһ�ݱ��պ�ͬ��Ҫô����Աǩ����Ҫô����˾ǩ����
					///	 * Ҳ����˵��"��������Ա"��"�����չ�˾"���������ԣ�������ͬʱ��ֵ </summary>
					///	 
						std::string personName;
					///	
					///	 <summary> * �����չ�˾������ </summary>
					///	 
						std::string companyName;
					///	
					///	 <summary> * ���տ�ʼ��Ч������ </summary>
					///	 
						long long beginDate;
					///	
					///	 <summary> * ����ʧЧ�����ڣ�һ������ڱ��տ�ʼ��Ч������ </summary>
					///	 
						long long endDate;
					///	
					///	 <summary> * ʾ������������ </summary>
					///	 
						std::string otherData;

					///	
					///	 <summary> * ���췽�������ʼ�����ͬ���ܷ��� </summary>
					///	 
					public:
						InsuranceContract(ConcreteBuilder *builder);

					///	
					///	 <summary> * ʾ�⣺���պ�ͬ��ĳЩ���� </summary>
					///	 
						virtual void someOperation();
					};

				}
			}
		}
	}
}