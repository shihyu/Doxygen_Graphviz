#pragma once

#include "InsuranceContract.h"
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
					/// <summary> * ���챣�պ�ͬ����Ĺ����� </summary>
					/// 
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
					///	 <summary> * ���췽�����������Ҫ�еĲ��� </summary>
					///	 * <param name="contractId"> ���պ�ͬ��� </param>
					///	 * <param name="beginDate"> ���տ�ʼ��Ч������ </param>
					///	 * <param name="endDate"> ����ʧЧ������ </param>
					///	 
					public:
						ConcreteBuilder(std::string contractId, long long beginDate, long long endDate);
					///	
					///	 <summary> * ѡ�����ݣ���������Ա������ </summary>
					///	 * <param name="personName">  ��������Ա������ </param>
					///	 * <returns> ���������� </returns>
					///	 
						virtual ConcreteBuilder *setPersonName(std::string personName);
					///	
					///	 <summary> *  ѡ�����ݣ������չ�˾������ </summary>
					///	 * <param name="companyName"> �����չ�˾������ </param>
					///	 * <returns> ���������� </returns>
					///	 
						virtual ConcreteBuilder *setCompanyName(std::string companyName);
					///	
					///	 <summary> * ѡ�����ݣ��������� </summary>
					///	 * <param name="otherData"> �������� </param>
					///	 * <returns> ���������� </returns>
					///	 
						virtual ConcreteBuilder *setOtherData(std::string otherData);
					///	
					///	 <summary> * ���������Ķ��󲢷��� </summary>
					///	 * <returns> �����ı��պ�ͬ�Ķ��� </returns>
					///	 
						virtual InsuranceContract *build();

						virtual std::string getContractId();

						virtual std::string getPersonName();

						virtual std::string getCompanyName();

						virtual long long getBeginDate();

						virtual long long getEndDate();

						virtual std::string getOtherData();
					};

				}
			}
		}
	}
}