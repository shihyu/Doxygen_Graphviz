#pragma once

#include "Observer.h"
#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace observer
			{
				namespace example3
				{


					///
					/// <summary> * Ŀ�������Ϊ���۲��ߣ�ʹ����ģ�� </summary>
					/// 
					class Subject
					{
					///	
					///	 <summary> * ��������ע��Ĺ۲��߶���Ҳ���Ǳ�ֽ�Ķ����� </summary>
					///	 
					private:
						std::vector<Observer*> readers;
					///	
					///	 <summary> * ��ֽ�Ķ�����Ҫ�����綩�ģ���Ҫע�� </summary>
					///	 * <param name="reader"> ��ֽ�Ķ���  </param>
					///	 * <returns> �Ƿ�ע��ɹ� </returns>
					///	 
					public:
						virtual void attach(Observer *reader);
					///	
					///	 <summary> * ��ֽ�Ķ��߿���ȡ������ </summary>
					///	 * <param name="reader"> ��ֽ�Ķ��� </param>
					///	 * <returns> �Ƿ�ȡ���ɹ� </returns>
					///	 
						virtual void detach(Observer *reader);
					///	
					///	 <summary> * ��ÿ�ڱ�ֽӡˢ�����󣬾�ҪѸ�ٵ������ı��͵����ߵ����У�
					///	 * �൱��֪ͨ���ߣ�������֪�� </summary>
					///	 * <param name="content"> Ҫ�������͵����� </param>
					///	 
					protected:
						virtual void notifyObservers(std::string content);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								readers = std::vector<Observer*>();

								initialized = true;
							}
						}

public:
	Subject()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}