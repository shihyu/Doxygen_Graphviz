#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace observer
			{
				namespace example4
				{
					///
					/// <summary> * ��ֽ���󣬾����Ŀ��ʵ�� </summary>
					/// 
					class NewsPaper : public java::util::Observable
					{
					///	
					///	 <summary> * ��ֽ�ľ������� </summary>
					///	 
					private:
						std::string content;
					///	
					///	 <summary> * ��ȡ��ֽ�ľ������� </summary>
					///	 * <returns> ��ֽ�ľ������� </returns>
					///	 
					public:
						virtual std::string getContent();
					///	
					///	 <summary> * ʾ�⣬���ñ�ֽ�ľ������ݣ��൱��Ҫ���汨ֽ�� </summary>
					///	 * <param name="content"> ��ֽ�ľ������� </param>
					///	 
						virtual void setContent(std::string content);
					};

				}
			}
		}
	}
}