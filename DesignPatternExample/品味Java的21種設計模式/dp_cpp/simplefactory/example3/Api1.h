#ifndef _API_H_
#define _API_H_
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace simplefactory
			{
				namespace example3
				{
					///
					/// <summary> * ĳ���ӿ�(ͨ�õġ�����ġ��Ǿ���Ĺ��ܵ�)  </summary>
					/// 
                    class Api
                    {
					///	
					///	 <summary> * ĳ������Ĺ��ܷ����Ķ��壬��test1����ʾһ�¡�
					///	 * ����Ĺ��ܼܺ򵥣��Ѵ����s��ӡ�������  </summary>
					///	 * <param name="s"> ������Ҫ��ӡ������ַ��� </param>
					///	 
					public:
						virtual void test1(std::string) = 0;
					};

				}
			}
		}
	}
}
#endif
