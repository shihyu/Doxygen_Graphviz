#pragma once

#include "Flyweight.h"
#include "CacheConfModel.h"
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
				namespace example5
				{



					///
					/// <summary> * ��Ԫ������ͨ��ʵ�ֳ�Ϊ����
					/// * ����ʵ���������պ����ü����Ĺ��� </summary>
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
					///	 <summary> * �������汻�������Ļ������ã�keyֵ������map��һ�� </summary>
					///	 
						Map<std::string, CacheConfModel*> *cacheConfMap;
					///	
					///	 <summary> * ������¼����������õĴ�����keyֵ������map��һ�� </summary>
					///	 
						Map<std::string, int> *countMap;
					///	
					///	 <summary> * Ĭ�ϱ���6���ӣ���ҪΪ�˲��Է��㣬���ʱ����Ը���Ӧ�õ�Ҫ�����ã�����30���� </summary>
					///	 
						const long long DURABLE_TIME;

					///	
					///	 <summary> * ��ȡĳ����Ԫ��ʹ�õĴ��� </summary>
					///	 * <param name="key"> ��Ԫ��key </param>
					///	 * <returns> ��ʹ�õĴ��� </returns>
					///	 
					public:
						virtual int getUseTimes(std::string key);

					///	
					///	 <summary> * ��ȡkey��Ӧ����Ԫ���� </summary>
					///	 * <param name="key"> ��ȡ��Ԫ�����key </param>
					///	 * <returns> key��Ӧ����Ԫ���� </returns>
					///	 
						virtual Flyweight *getFlyweight(std::string key);
					///	
					///	 <summary> * ɾ��key��Ӧ����Ԫ�������������Ӧ�Ļ������ú����ô����ļ�¼�������� </summary>
					///	 * <param name="key"> Ҫɾ������Ԫ�����key </param>
					///	 
					private:
						void removeFlyweight(std::string key);
					///	
					///	 <summary> * ά�����������̣߳��ڲ�ʹ�� </summary>
					///	 
					private:
						class ClearCache : public Thread
						{
						public:
							virtual void run();
						};

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								fsMap = std::map<std::string, Flyweight*>();
								cacheConfMap = std::map<std::string, CacheConfModel*>();
								countMap = std::map<std::string, int>();
								DURABLE_TIME = 6*1000L;

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}