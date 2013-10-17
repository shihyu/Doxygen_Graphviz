#pragma once

#include "Flyweight.h"
#include "AuthorizationFlyweight.h"
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
					/// <summary> * ��Ԫ������ͨ��ʵ�ֳ�Ϊ���� </summary>
					/// 
					class FlyweightFactory
					{
					private:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static FlyweightFactory *factory = new FlyweightFactory();
						FlyweightFactory();
					public:
						static FlyweightFactory *getInstance();
					///	
					///	 <summary> * ������flyweight���� </summary>
					///	 
					private:
						Map<std::string, Flyweight*> *fsMap;
					///	
					///	 <summary> * ��ȡkey��Ӧ����Ԫ���� </summary>
					///	 * <param name="key"> ��ȡ��Ԫ�����key </param>
					///	 * <returns> key��Ӧ����Ԫ���� </returns>
					///	 
					public:
						virtual Flyweight *getFlyweight(std::string key);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								fsMap = std::map<std::string, Flyweight*>();

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}