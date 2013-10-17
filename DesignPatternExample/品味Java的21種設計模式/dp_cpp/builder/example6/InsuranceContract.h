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
				namespace example6
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
					///	 <summary> * ���췽�������ʼ�����˽�е� </summary>
					///	 
						InsuranceContract(ConcreteBuilder *builder);

					///	
					///	 <summary> * ���챣�պ�ͬ����Ĺ����� </summary>
					///	 
					public:
						class ConcreteBuilder
						{
						private:
							std::string contractId;
							std::string personName;
							std::string companyName;
							long long beginDate;
							long long endDate;
							std::string otherData;
					///		
					///		 <summary> * ���췽�����������Ҫ�еĲ��� </summary>
					///		 * <param name="contractId"> ���պ�ͬ��� </param>
					///		 * <param name="beginDate"> ���տ�ʼ��Ч������ </param>
					///		 * <param name="endDate"> ����ʧЧ������ </param>
					///		 
						public:
							ConcreteBuilder(std::string contractId, long long beginDate, long long endDate);
					///		
					///		 <summary> * ѡ�����ݣ���������Ա������ </summary>
					///		 * <param name="personName">  ��������Ա������ </param>
					///		 * <returns> ���������� </returns>
					///		 
							virtual ConcreteBuilder *setPersonName(std::string personName);
					///		
					///		 <summary> *  ѡ�����ݣ������չ�˾������ </summary>
					///		 * <param name="companyName"> �����չ�˾������ </param>
					///		 * <returns> ���������� </returns>
					///		 
							virtual ConcreteBuilder *setCompanyName(std::string companyName);
					///		
					///		 <summary> * ѡ�����ݣ��������� </summary>
					///		 * <param name="otherData"> �������� </param>
					///		 * <returns> ���������� </returns>
					///		 
							virtual ConcreteBuilder *setOtherData(std::string otherData);
					///		
					///		 <summary> * ���������Ķ��󲢷��� </summary>
					///		 * <returns> �����ı��պ�ͬ�Ķ��� </returns>
					///		 
							virtual InsuranceContract *build();
						};

					///	
					///	 <summary> * ʾ�⣺���պ�ͬ��ĳЩ���� </summary>
					///	 
					public:
						virtual void someOperation();
					};

				}
			}
		}
	}
}