#pragma once

#include "Flyweight.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace flyweight
			{
				namespace example4
				{


					///
					/// <summary> * ��װ��Ȩ�������ظ����ֲ��ֵ���Ԫ���� </summary>
					/// 
					class AuthorizationFlyweight : public Flyweight
					{
					///	
					///	 <summary> * �ڲ�״̬����ȫʵ�� </summary>
					///	 
					private:
						std::string securityEntity;
					///	
					///	 <summary> * �ڲ�״̬��Ȩ�� </summary>
					///	 
						std::string permit;
					///	
					///	 <summary> * ���췽��������״̬���� </summary>
					///	 * <param name="state"> ״̬���ݣ�������ȫʵ���Ȩ�޵����ݣ���","�ָ� </param>
					///	 
					public:
						AuthorizationFlyweight(std::string state);

						virtual std::string getSecurityEntity();
						virtual std::string getPermit();

						virtual bool match(std::string securityEntity, std::string permit);

						virtual void add(Flyweight *f);
					};

				}
			}
		}
	}
}